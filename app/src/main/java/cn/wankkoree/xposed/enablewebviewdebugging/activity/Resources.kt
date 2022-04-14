package cn.wankkoree.xposed.enablewebviewdebugging.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cn.wankkoree.xposed.enablewebviewdebugging.R
import cn.wankkoree.xposed.enablewebviewdebugging.ResourcesVersionAlreadyExisted
import cn.wankkoree.xposed.enablewebviewdebugging.activity.component.Tag
import cn.wankkoree.xposed.enablewebviewdebugging.data.ResourcesSP
import cn.wankkoree.xposed.enablewebviewdebugging.data.getSet
import cn.wankkoree.xposed.enablewebviewdebugging.data.put
import cn.wankkoree.xposed.enablewebviewdebugging.data.remove
import cn.wankkoree.xposed.enablewebviewdebugging.databinding.ResourcesBinding
import cn.wankkoree.xposed.enablewebviewdebugging.http.Http
import com.google.gson.Gson
import com.highcapable.yukihookapi.hook.factory.modulePrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Resources : AppCompatActivity() {
    private lateinit var viewBinding: ResourcesBinding
    private var toast: Toast? = null
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ResourcesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        context = this

        lifecycleScope.launch(Dispatchers.Main) {
            refresh()
            val vConsoleVersionStr = try {
                Http.get("https://data.jsdelivr.com/v1/package/npm/vconsole")
            } catch(e: Exception) {
                Log.e(getString(R.string.app_name), getString(R.string.pull_failed).format(getString(R.string.vconsole)), e)
                null
            }
            if (vConsoleVersionStr != null) {
                val vConsoleVersion = Gson().fromJson(vConsoleVersionStr, cn.wankkoree.xposed.enablewebviewdebugging.http.bean.api.npm.Versions::class.java)
                val adapter = ArrayAdapter(context, R.layout.spinner_item, vConsoleVersion.versions)
                adapter.setDropDownViewResource(R.layout.spinner_item)
                viewBinding.resourcesVconsoleVersion.adapter = adapter
                viewBinding.resourcesVconsoleVersion.setSelection(adapter.getPosition(vConsoleVersion.tags.latest))
            } else {
                toast?.cancel()
                toast = Toast.makeText(context, getString(R.string.pull_failed).format(getString(R.string.vconsole)), Toast.LENGTH_SHORT)
                toast!!.show()
            }

            val nebulaUCSDKVersionStr = try {
                Http.get("https://api.github.com/repos/WankkoRee/EnableWebViewDebugging-Rules/contents/resources/nebulaucsdk")
            } catch(e: Exception) {
                Log.e(getString(R.string.app_name), getString(R.string.pull_failed).format(getString(R.string.nebulaucsdk)), e)
                null
            }
            if (nebulaUCSDKVersionStr != null) {
                val nebulaUCSDKVersion = Gson().fromJson(nebulaUCSDKVersionStr, Array<cn.wankkoree.xposed.enablewebviewdebugging.http.bean.api.github.RepoContent>::class.java)
                val adapter = ArrayAdapter(context, R.layout.spinner_item, nebulaUCSDKVersion.map{ it.name })
                adapter.setDropDownViewResource(R.layout.spinner_item)
                viewBinding.resourcesNebulaucsdkVersion.adapter = adapter
                viewBinding.resourcesNebulaucsdkVersion.setSelection(0)
            } else {
                toast?.cancel()
                toast = Toast.makeText(context, getString(R.string.pull_failed).format(getString(R.string.nebulaucsdk)), Toast.LENGTH_SHORT)
                toast!!.show()
            }
        }

        viewBinding.resourcesToolbarBack.setOnClickListener {
            finish()
        }
        viewBinding.resourcesVconsoleCard.setOnClickListener { tips() }
        viewBinding.resourcesNebulaucsdkCard.setOnClickListener { tips() }
        viewBinding.resourcesVconsoleDownload.setOnClickListener { lifecycleScope.launch(Dispatchers.Main) {
            val version = viewBinding.resourcesVconsoleVersion.selectedItem as String
            toast?.cancel()
            toast = Toast.makeText(context, "${getString(R.string.download_started)} ${getString(R.string.vconsole)}$version", Toast.LENGTH_SHORT)
            toast!!.show()
            val vConsoleStr = try {
                Http.get("https://cdn.jsdelivr.net/npm/vconsole@$version/dist/vconsole.min.js")
            } catch(e: Exception) {
                Log.e(getString(R.string.app_name), getString(R.string.download_failed), e)
                null
            }
            if (vConsoleStr != null) {
                toast?.cancel()
                toast = Toast.makeText(context, getString(R.string.download_completed), Toast.LENGTH_SHORT)
                toast!!.show()
                modulePrefs.run {
                    name("resources_vConsole_$version")
                    putString("vConsole", vConsoleStr)
                    name("resources")
                    try { put(ResourcesSP.vConsole_versions, version) } catch (e: ResourcesVersionAlreadyExisted) {
                        toast?.cancel()
                        toast = Toast.makeText(context, getString(R.string.the_target_version_already_exists_it_will_be_overwritten), Toast.LENGTH_SHORT)
                        toast!!.show()
                    }
                }
                refresh()
            } else {
                toast?.cancel()
                toast = Toast.makeText(context, getString(R.string.download_failed), Toast.LENGTH_SHORT)
                toast!!.show()
            }
        } }
        viewBinding.resourcesNebulaucsdkDownload.setOnClickListener { lifecycleScope.launch(Dispatchers.Main) {
            val version = viewBinding.resourcesNebulaucsdkVersion.selectedItem as String
            toast?.cancel()
            toast = Toast.makeText(context, "${getString(R.string.download_started)} ${getString(R.string.nebulaucsdk)}$version", Toast.LENGTH_SHORT)
            toast!!.show()
            val nebulaUCSDKArm64V8aBin = try {
                Http.getBytes("https://raw.githubusercontent.com/WankkoRee/EnableWebViewDebugging-Rules/master/resources/nebulaucsdk/$version/arm64-v8a.so")
            } catch(e: Exception) {
                Log.e(getString(R.string.app_name), getString(R.string.download_failed), e)
                null
            }
            val nebulaUCSDKArmeabiV7aBin = try {
                Http.getBytes("https://raw.githubusercontent.com/WankkoRee/EnableWebViewDebugging-Rules/master/resources/nebulaucsdk/$version/armeabi-v7a.so")
            } catch(e: Exception) {
                Log.e(getString(R.string.app_name), getString(R.string.download_failed), e)
                null
            }
            if (nebulaUCSDKArm64V8aBin != null && nebulaUCSDKArmeabiV7aBin != null) {
                toast?.cancel()
                toast = Toast.makeText(context, getString(R.string.download_completed), Toast.LENGTH_SHORT)
                toast!!.show()
                modulePrefs.run {
                    name("resources_nebulaUCSDK_$version")
                    putString("nebulaUCSDK_arm64-v8a", Base64.encodeToString(nebulaUCSDKArm64V8aBin, Base64.NO_WRAP))
                    putString("nebulaUCSDK_armeabi-v7a", Base64.encodeToString(nebulaUCSDKArmeabiV7aBin, Base64.NO_WRAP))
                    name("resources")
                    try { put(ResourcesSP.nebulaUCSDK_versions, version) } catch (e: ResourcesVersionAlreadyExisted) {
                        toast?.cancel()
                        toast = Toast.makeText(context, getString(R.string.the_target_version_already_exists_it_will_be_overwritten), Toast.LENGTH_SHORT)
                        toast!!.show()
                    }
                }
                refresh()
            } else {
                toast?.cancel()
                toast = Toast.makeText(context, getString(R.string.download_failed), Toast.LENGTH_SHORT)
                toast!!.show()
            }
        } }
    }

    private fun refresh() {
        val vConsoleVersions: HashSet<String>
        val nebulaUCSDKVersions: HashSet<String>
        modulePrefs("resources").run {
            vConsoleVersions = getSet(ResourcesSP.vConsole_versions)
            nebulaUCSDKVersions = getSet(ResourcesSP.nebulaUCSDK_versions)
        }
        viewBinding.resourcesVconsoleLocal.run {
            removeAllViews()
            vConsoleVersions.forEach { vConsoleVersion ->
                addView(Tag(context).also {
                    it.text = vConsoleVersion
                    it.color = getColor(R.color.backgroundInfo)
                    it.isLongClickable = true
                    it.setOnLongClickListener { t ->
                        val version = (t as Tag).text as String
                        modulePrefs.run {
                            name("resources")
                            remove(ResourcesSP.vConsole_versions, version)
                            name("resources_vConsole_$version")
                            remove("vConsole")
                        }
                        toast?.cancel()
                        toast = Toast.makeText(context, getString(R.string.delete_completed), Toast.LENGTH_SHORT)
                        toast!!.show()
                        refresh()
                        true
                    }
                })
            }
        }
        viewBinding.resourcesNebulaucsdkLocal.run {
            removeAllViews()
            nebulaUCSDKVersions.forEach { nebulaUCSDKVersion ->
                addView(Tag(context).also {
                    it.text = nebulaUCSDKVersion
                    it.color = getColor(R.color.backgroundInfo)
                    it.isLongClickable = true
                    it.setOnLongClickListener { t ->
                        val version = (t as Tag).text as String
                        modulePrefs.run {
                            name("resources")
                            remove(ResourcesSP.nebulaUCSDK_versions, version)
                            name("resources_nebulaUCSDK_$version")
                            remove("nebulaUCSDK_arm64-v8a")
                            remove("nebulaUCSDK_armeabi-v7a")
                        }
                        toast?.cancel()
                        toast = Toast.makeText(context, getString(R.string.delete_completed), Toast.LENGTH_SHORT)
                        toast!!.show()
                        refresh()
                        true
                    }
                })
            }
        }
    }

    private fun tips() {
        toast?.cancel()
        toast = Toast.makeText(context, getString(R.string.please_click_the_download_button_instead_of_here_long_press_on_a_version_tag_to_delete_it), Toast.LENGTH_SHORT)
        toast!!.show()
    }
}
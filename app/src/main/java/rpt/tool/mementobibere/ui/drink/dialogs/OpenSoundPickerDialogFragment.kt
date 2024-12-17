package rpt.tool.mementobibere.ui.drink.dialogs

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import rpt.tool.mementobibere.BaseDialogFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.databinding.DialogSoundPickBinding
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.data.SoundModel
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.utils.view.adapters.SoundAdapter

class OpenSoundPickerDialogFragment:BaseDialogFragment<DialogSoundPickBinding>(DialogSoundPickBinding::inflate) {

    var soundAdapter: SoundAdapter? = null
    var lst_sounds: ArrayList<SoundModel> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        loadSounds()

        binding.btnCancel.setOnClickListener { dialog!!.dismiss() }

        binding.btnSave.setOnClickListener {
            for (k in lst_sounds.indices) {
                if (lst_sounds[k].isSelected) {
                    SharedPreferencesManager.reminderSound = k
                    break
                }
            }
            dialog!!.dismiss()
        }

        soundAdapter = SoundAdapter(requireActivity(), lst_sounds, object : SoundAdapter.CallBack {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClickSelect(time: SoundModel?, position: Int) {
                for (k in lst_sounds.indices) {
                    lst_sounds[k].isSelected(false)
                }

                lst_sounds[position].isSelected(true)
                soundAdapter!!.notifyDataSetChanged()

                playSound(position)
            }
        })

        binding.soundRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.soundRecyclerView.adapter = soundAdapter
    }

    private fun loadSounds() {
        lst_sounds.clear()

        lst_sounds.add(getSoundModel(0, "Default"))
        lst_sounds.add(getSoundModel(1, "Bell"))
        lst_sounds.add(getSoundModel(2, "Blop"))
        lst_sounds.add(getSoundModel(3, "Bong"))
        lst_sounds.add(getSoundModel(4, "Click"))
        lst_sounds.add(getSoundModel(5, "Echo droplet"))
        lst_sounds.add(getSoundModel(6, "Mario droplet"))
        lst_sounds.add(getSoundModel(7, "Ship bell"))
        lst_sounds.add(getSoundModel(8, "Simple droplet"))
        lst_sounds.add(getSoundModel(9, "Tiny droplet"))
    }

    private fun getSoundModel(index: Int, name: String?): SoundModel {
        val soundModel = SoundModel()
        soundModel.id = index
        soundModel.name = name
        soundModel.isSelected(SharedPreferencesManager.reminderSound == index)

        return soundModel
    }

    fun playSound(idx: Int) {
        var mp: MediaPlayer? = null

        when (idx) {
            0 -> mp = MediaPlayer.create(requireContext(), Settings.System.DEFAULT_NOTIFICATION_URI)
            1 -> mp = MediaPlayer.create(requireContext(), R.raw.bell)
            2 -> mp = MediaPlayer.create(requireContext(), R.raw.blop)
            3 -> mp = MediaPlayer.create(requireContext(), R.raw.bong)
            4 -> mp = MediaPlayer.create(requireContext(), R.raw.click)
            5 -> mp = MediaPlayer.create(requireContext(), R.raw.echo_droplet)
            6 -> mp = MediaPlayer.create(requireContext(), R.raw.mario_droplet)
            7 -> mp = MediaPlayer.create(requireContext(), R.raw.ship_bell)
            8 -> mp = MediaPlayer.create(requireContext(), R.raw.simple_droplet)
            9 -> mp = MediaPlayer.create(requireContext(), R.raw.tiny_droplet)
        }

        mp!!.start()
    }
}
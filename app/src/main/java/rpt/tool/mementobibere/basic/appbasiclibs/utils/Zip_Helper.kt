package rpt.tool.mementobibere.basic.appbasiclibs.utils

import android.content.Context
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class Zip_Helper
    (var mContext: Context) {
    fun zip(_files: Array<String>, zipFileName: String?) {
        val BUFFER: Int = 1024
        try {
            var origin: BufferedInputStream? = null
            val dest: FileOutputStream = FileOutputStream(zipFileName)
            val out: ZipOutputStream = ZipOutputStream(
                BufferedOutputStream(
                    dest
                )
            )
            val data: ByteArray = ByteArray(BUFFER)

            for (i in _files.indices) {
                val fi: FileInputStream = FileInputStream(_files.get(i))
                origin = BufferedInputStream(fi, BUFFER)

                val entry: ZipEntry =
                    ZipEntry(_files.get(i).substring(_files.get(i).lastIndexOf("/") + 1))
                out.putNextEntry(entry)
                var count: Int

                while ((origin.read(data, 0, BUFFER).also({ count = it })) != -1) {
                    out.write(data, 0, count)
                }
                origin.close()
            }

            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun zip(_files: ArrayList<String>, zipFileName: String?) {
        val BUFFER: Int = 1024
        try {
            var origin: BufferedInputStream? = null
            val dest: FileOutputStream = FileOutputStream(zipFileName)
            val out: ZipOutputStream = ZipOutputStream(
                BufferedOutputStream(
                    dest
                )
            )
            val data: ByteArray = ByteArray(BUFFER)

            for (i in _files.indices) {
                val fi: FileInputStream = FileInputStream(_files.get(i))
                origin = BufferedInputStream(fi, BUFFER)

                val entry: ZipEntry =
                    ZipEntry(_files.get(i).substring(_files.get(i).lastIndexOf("/") + 1))
                out.putNextEntry(entry)
                var count: Int

                while ((origin.read(data, 0, BUFFER).also({ count = it })) != -1) {
                    out.write(data, 0, count)
                }
                origin.close()
            }

            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @Throws(IOException::class)
        fun unzip(zipFile: String?, location: String) {
            try {
                val f: File = File(location)
                if (!f.isDirectory()) {
                    f.mkdirs()
                }
                val zin: ZipInputStream = ZipInputStream(FileInputStream(zipFile))
                try {
                    var ze: ZipEntry? = null
                    while ((zin.getNextEntry().also({ ze = it })) != null) {
                        val path: String = location + File.separator + ze!!.getName()

                        if (ze!!.isDirectory()) {
                            val unzipFile: File = File(path)
                            if (!unzipFile.isDirectory()) {
                                unzipFile.mkdirs()
                            }
                        } else {
                            val fout: FileOutputStream = FileOutputStream(path, false)

                            try {
                                var c: Int = zin.read()
                                while (c != -1) {
                                    fout.write(c)
                                    c = zin.read()
                                }
                                zin.closeEntry()
                            } finally {
                                fout.close()
                            }
                        }
                    }
                } finally {
                    zin.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
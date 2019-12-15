package pumpkin.nbt.loader

import pumpkin.nbt.NBTCompound
import pumpkin.nbt.readNBTCompound
import java.io.DataInputStream
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.GZIPInputStream

/**
 * Base type for guided loading of NBT-based resources.
 */
abstract class NBTLoader {

    protected open fun load(stream: InputStream, compressed: Boolean = false): NBTCompound =
        if (compressed) {
            DataInputStream(GZIPInputStream(stream)).readNBTCompound()
        } else {
            DataInputStream(stream).readNBTCompound()
        }

    protected open fun load(file: Path, compressed: Boolean = false): NBTCompound =
        Files.newInputStream(file).use { load(it, compressed) }

    protected open fun load(url: URL, compressed: Boolean = false): NBTCompound =
        url.openStream().use { load(it, compressed) }
}
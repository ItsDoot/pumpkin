package pumpkin.util

import java.nio.file.Path

operator fun Path.div(child: Path): Path = this.resolve(child)

operator fun Path.div(child: String): Path = this.resolve(child)

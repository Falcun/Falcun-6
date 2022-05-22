package falcun.net.api.fonts

trait FalcunFont {
  def drawString(text: String, x: Number, y: Number, color: Int, underline: Boolean): Unit

  def getStringWidth(text: String): Number

  def stringHeight(text: String): Number

  def stringHeight = 9
}

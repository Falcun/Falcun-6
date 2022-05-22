package falcun.net.api.gui.animations

final class AnimationTimer(var start: Int, var end: Int, var duration: Long) {
  creation = System.currentTimeMillis
  var creation = 0L

  def getProgress: Int = {
    val dif = (System.currentTimeMillis - creation).toDouble
    var percent = dif / duration
    if (percent > 1) percent = 1
    val gap = end - start
    val progress = (percent * gap).toInt
    start + progress
  }

  def isDone: Boolean = System.currentTimeMillis > creation + duration

  def swapDirection(): Unit = {
    val now = System.currentTimeMillis
    var remaingTime = duration + creation - now
    if (remaingTime < 0) remaingTime = 0
    creation = now - remaingTime
    val s = start
    start = end
    end = s
  }
}

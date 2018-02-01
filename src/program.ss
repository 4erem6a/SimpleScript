//SimpleScript v1.9.2:
require "io"
require "canvas"
require "async"

import canvas.Color
import canvas.Paint
import canvas.Window

let red = new Paint(new Color(255, 0, 0))
let green = new Paint(new Color(0, 255, 0))

Window.open()

red.drawLine(0, 0, 200, 200)
green.drawLine(0, 200, 200, 0)

async.sleep(3000)

Window.close()
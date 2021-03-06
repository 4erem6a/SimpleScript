class Color {
    new(r, g, b, a = 255) {
        this.red = r
        this.green = g
        this.blue = b
        this.alpha = a
    }
    locked getCode()
        -> require("jcanvas").rgba(this.red, this.green, this.blue, this.alpha)
    locked obj()
        -> {red: that.red, green: that.green, blue: that.blue, alpha: that.alpha}
    locked $call() -> this.getCode()
}

class Paint {
    new(color = undefined) {
        this.setColor(color)
    }
    locked setColor(color) {
        let canvas = require("canvas")
        if (color is canvas.Color)
            this.color = color
        else if (this.color == undefined)
            this.color = new canvas.Color(0, 0, 0)
    }
    locked applyColor() -> require("jcanvas").setColor(this.color.getCode())
    locked drawLine(x1, y1, x2, y2) {
        this.applyColor()
        let jcanvas = require("jcanvas")
        jcanvas.line(x1, y1, x2, y2)
        jcanvas.repaint()
    }
    locked drawOval(x, y, h, w) {
        this.applyColor()
        let jcanvas = require("jcanvas")
        jcanvas.oval(x, y, h, w)
        jcanvas.repaint()
    }
    locked fillOval(x, y, h, w) {
        this.applyColor()
        let jcanvas = require("jcanvas")
        jcanvas.foval(x, y, h, w)
        jcanvas.repaint()
    }
    locked drawRect(x, y, h, w) {
        this.applyColor()
        let jcanvas = require("jcanvas")
        jcanvas.rect(x, y, h, w)
        jcanvas.repaint()
    }
    locked fillRect(x, y, h, w) {
        this.applyColor()
        let jcanvas = require("jcanvas")
        jcanvas.frect(x, y, h, w)
        jcanvas.repaint()
    }
    locked drawClip(x, y, h, w) {
        this.applyColor()
        let jcanvas = require("jcanvas")
        jcanvas.clip(x, y, h, w)
        jcanvas.repaint()
    }
    locked drawText(x, y, text) {
        this.applyColor()
        let jcanvas = require("jcanvas")
        jcanvas.text(x, y, text)
        jcanvas.repaint()
    }
    locked $call(color) -> this.setColor(color)
}

class Window {
    static locked open(title = "", w = 640, h = 480)
        require("jcanvas").createWindow(title, w, h)
    static locked close()
        require("jcanvas").closeWindow()
}

exports {
    Color: Color,
    Paint: Paint,
    Window: Window,
    input: require("jcanvas").input
}
package dev.enjarai.minitardis.component.screen.element;

import dev.enjarai.minitardis.block.console.ScreenBlockEntity;
import dev.enjarai.minitardis.canvas.TardisCanvasUtils;
import dev.enjarai.minitardis.component.TardisControl;
import dev.enjarai.minitardis.component.screen.canvas.patbox.DrawableCanvas;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ClickType;

public class FloppyBirdElement extends PlacedElement {
    public float gradualX;
    public float gradualY;
    public float deltaX;
    public float deltaY;

    public FloppyBirdElement(int x, int y) {
        super(x, y, 8, 8);
        gradualX = x;
        gradualY = y;
    }

    @Override
    public void draw(TardisControl controls, ScreenBlockEntity blockEntity, dev.enjarai.minitardis.component.screen.canvas.patbox.DrawableCanvas canvas) {
        gradualX += deltaX;
        gradualY += deltaY;
        x = (int) gradualX;
        y = (int) gradualY;
        super.draw(controls, blockEntity, canvas);
    }

    @Override
    protected void drawElement(TardisControl controls, ScreenBlockEntity blockEntity, DrawableCanvas canvas) {
        canvas.draw(0, 0, TardisCanvasUtils.getSprite("floppy_bird"));
    }

    @Override
    protected boolean onClickElement(TardisControl controls, ScreenBlockEntity blockEntity, ServerPlayerEntity player, ClickType type, int x, int y) {
        return false;
    }
}

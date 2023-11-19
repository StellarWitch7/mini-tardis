package dev.enjarai.minitardis.component.screen;

import com.mojang.serialization.Codec;
import dev.enjarai.minitardis.MiniTardis;
import dev.enjarai.minitardis.block.console.ConsoleScreenBlockEntity;
import dev.enjarai.minitardis.canvas.ModCanvasUtils;
import dev.enjarai.minitardis.component.TardisControl;
import eu.pb4.mapcanvas.api.core.CanvasColor;
import eu.pb4.mapcanvas.api.core.DrawableCanvas;
import eu.pb4.mapcanvas.api.font.DefaultFonts;
import eu.pb4.mapcanvas.api.utils.CanvasUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;

public class GpsApp implements ScreenApp {
    public static final Codec<GpsApp> CODEC = Codec.unit(GpsApp::new);
    public static final Identifier ID = MiniTardis.id("gps");

    @Override
    public void draw(TardisControl controls, ConsoleScreenBlockEntity blockEntity, DrawableCanvas canvas) {
        var destination = controls.getTardis().getDestination();
        DefaultFonts.VANILLA.drawText(canvas, "Current Location", 3, 4, 8, CanvasColor.WHITE_HIGH);
        DefaultFonts.VANILLA.drawText(canvas,
                destination.map(l -> l.pos().getX() + " " + l.pos().getY() + " " + l.pos().getZ()).orElse("Unknown"),
                3, 4 + 9, 8, CanvasColor.WHITE_HIGH);
    }

    @Override
    public boolean onClick(TardisControl controls, ConsoleScreenBlockEntity blockEntity, ServerPlayerEntity player, ClickType type, int x, int y) {
        return false;
    }

    @Override
    public void drawIcon(TardisControl controls, ConsoleScreenBlockEntity blockEntity, DrawableCanvas canvas) {
        CanvasUtils.draw(canvas, 0, 0, ModCanvasUtils.GPS_APP);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}

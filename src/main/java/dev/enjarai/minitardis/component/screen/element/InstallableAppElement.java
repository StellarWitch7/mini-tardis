package dev.enjarai.minitardis.component.screen.element;

import dev.enjarai.minitardis.block.console.ScreenBlockEntity;
import dev.enjarai.minitardis.canvas.TardisCanvasUtils;
import dev.enjarai.minitardis.component.TardisControl;
import dev.enjarai.minitardis.component.screen.app.ScreenApp;
import dev.enjarai.minitardis.component.screen.canvas.patbox.DrawableCanvas;
import dev.enjarai.minitardis.component.screen.canvas.patbox.SubView;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ClickType;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class InstallableAppElement extends PlacedElement {
    public final ScreenApp app;
    public final boolean installed;
    private final AppSelectorElement parent;
    private final Consumer<InstallableAppElement> onSelect;
    private final BiFunction<ScreenBlockEntity, InstallableAppElement, Boolean> moveExecutor;

    public InstallableAppElement(int x, int y, ScreenApp app, boolean installed, AppSelectorElement parent, Consumer<InstallableAppElement> onSelect, BiFunction<ScreenBlockEntity, InstallableAppElement, Boolean> moveExecutor) {
        super(x, y, 26, 26);
        this.app = app;
        this.installed = installed;
        this.parent = parent;
        this.onSelect = onSelect;
        this.moveExecutor = moveExecutor;
    }

    @Override
    protected void drawElement(TardisControl controls, ScreenBlockEntity blockEntity, DrawableCanvas canvas) {
        app.drawIcon(controls, blockEntity, new SubView(canvas, 1, 1, 24, 24));
        if (isSelected()) {
            canvas.draw(0, 0, TardisCanvasUtils.getSprite("app_selected"));
        }
    }

    @Override
    protected boolean onClickElement(TardisControl controls, ScreenBlockEntity blockEntity, ServerPlayerEntity player, ClickType type, int x, int y) {
        if (!isSelected()) {
            parent.selected = this;
            onSelect.accept(this);
            blockEntity.playClickSound(1.6f);
        } else {
            if (moveExecutor.apply(blockEntity, this)) {
                blockEntity.playClickSound(1.8f);
            } else {
                blockEntity.playClickSound(1.4f);
            }
        }
        return true;
    }

    public boolean isSelected() {
        return parent.selected == this;
    }
}

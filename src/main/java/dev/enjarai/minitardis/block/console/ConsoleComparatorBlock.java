package dev.enjarai.minitardis.block.console;

import dev.enjarai.minitardis.block.TardisAware;
import dev.enjarai.minitardis.component.TardisControl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class ConsoleComparatorBlock extends AbstractGateConsoleBlock implements ConsoleInput, TardisAware {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<ComparatorMode> COMPARATOR_MODE = Properties.COMPARATOR_MODE;
    public static final BooleanProperty POWERED = Properties.POWERED;

    private final BiFunction<TardisControl, Boolean, Boolean> controlInput;

    public ConsoleComparatorBlock(Settings settings, BiFunction<TardisControl, Boolean, Boolean> controlInput) {
        super(settings);
        this.controlInput = controlInput;
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(COMPARATOR_MODE, ComparatorMode.COMPARE)
                .with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, COMPARATOR_MODE, POWERED);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            world.setBlockState(pos, state.cycle(COMPARATOR_MODE), Block.NOTIFY_ALL);
            var newMode = world.getBlockState(pos).get(COMPARATOR_MODE);
            float f = newMode == ComparatorMode.SUBTRACT ? 0.55F : 0.5F;
            world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f);
            if (!getTardis(world).map(tardis -> controlInput.apply(tardis.getControls(), newMode == ComparatorMode.SUBTRACT)).orElse(false)) {
                inputFailure(world, pos, SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 3);
            }
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.get(FACING) && neighborState.getBlock() instanceof ConsoleComparatorDependentBlock) {
            return state.with(POWERED, neighborState.get(ButtonBlock.POWERED));
        }
        return state.with(POWERED, false);
    }
}

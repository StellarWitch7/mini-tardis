package dev.enjarai.minitardis.block;

import net.minecraft.block.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static dev.enjarai.minitardis.block.TardisExteriorExtensionBlock.VISIBLENESS;

@SuppressWarnings("deprecation")
public class TardisExteriorBlock extends BlockWithEntity {
    public static final MapCodec<TardisExteriorBlock> CODEC = createCodec(TardisExteriorBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public static final VoxelShape OUTLINE_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(-1, 2, -1, 17, 16, 17),
            Block.createCuboidShape(-3, 0, -3, 19, 2, 19),
            Block.createCuboidShape(-2, 2, -2, 0, 16, 0),
            Block.createCuboidShape(16, 2, -2, 18, 16, 0),
            Block.createCuboidShape(16, 2, 16, 18, 16, 18),
            Block.createCuboidShape(-2, 2, 16, 0, 16, 18)
    );

    protected TardisExteriorBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TardisExteriorBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (hit.getSide() == state.get(FACING)) {
            if (!world.isClient()
                    && world.getBlockEntity(pos) instanceof TardisExteriorBlockEntity blockEntity) {
                blockEntity.teleportEntityIn(player);
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hit);
    }

    // Disabled because of silly transparency issues on block outlines
//    @Override
//    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
//        return OUTLINE_SHAPE;
//    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.setBlockState(pos.up(), ModBlocks.TARDIS_EXTERIOR_EXTENSION.getStateWithProperties(state).with(VISIBLENESS, 0));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlocks.TARDIS_EXTERIOR_ENTITY, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}

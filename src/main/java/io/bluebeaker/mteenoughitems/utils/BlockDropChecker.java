package io.bluebeaker.mteenoughitems.utils;

import com.mojang.authlib.GameProfile;
import io.bluebeaker.mteenoughitems.MTEEnoughItems;
import io.bluebeaker.mteenoughitems.utils.player.CheckerFakePlayer;
import io.bluebeaker.mteenoughitems.utils.world.DummyWorld;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class BlockDropChecker {
    private static final DummyWorld world = new DummyWorld();
    private static final CheckerFakePlayer fakePlayer = new CheckerFakePlayer(world,new GameProfile(UUID.randomUUID(),"MTEEnoughItems_FakePlayer"));
    private static final BlockPos pos = new BlockPos(0,63,0);

    private static final Vec3d rayStart = new Vec3d(-1,62,-1);
    private static final Vec3d rayEnd = new Vec3d(1,64,1);

    public static ItemStack getDrop(IBlockState state){
        try {
            world.setBlockState(pos,state, 20);
            return state.getBlock().getPickBlock(state,state.collisionRayTrace(world,pos,rayStart,rayEnd),world,pos,fakePlayer);
        } catch (Throwable e) {
            MTEEnoughItems.getLogger().error("Error getting item for blockstate {}:",state,e);
        }
        return ItemStack.EMPTY;
    }

    public static Map<ItemStack,Set<IBlockState>> getGroupedDrops(Collection<IBlockState> blockStates){
        Map<String,ItemStack> addedItems = new HashMap<>();
        Map<ItemStack,Set<IBlockState>> blocksMap = new HashMap<>();

        for (IBlockState blockState : blockStates) {
            ItemStack drop = getDrop(blockState);
            String item = drop.serializeNBT().toString();

            if(!addedItems.containsKey(item)){
                addedItems.put(item,drop);
            }else {
                drop = addedItems.get(item);
            }

            blocksMap.computeIfAbsent(drop,(i)-> new HashSet<>());
            blocksMap.get(drop).add(blockState);
        }
        return blocksMap;
    }
}

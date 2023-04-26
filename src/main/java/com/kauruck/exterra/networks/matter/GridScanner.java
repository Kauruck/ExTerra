package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.util.PositionTuple;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GridScanner {

    public static Grid ScanGrid(BlockPos center, int size, Level level){
        Grid grid = new Grid(size, center);
        for(int x = -size; x < size + 1; x ++){
            for(int z = -size; z < size + 1; z ++) {
                BlockPos current = center.offset(x, 0, z);
                grid.setCell(current, level.getBlockState(current));
            }
        }

        return grid;
    }

    public static HashSet<Wire> scanGridForWires(Grid grid){
        List<Tuple<Integer, Integer>> startPoints = scanGridForMembersInternal(grid);
        HashSet<Wire> out = new HashSet<>();
        for(Tuple<Integer, Integer> currentPoint : startPoints){
            int x = currentPoint.getA();
            int z = currentPoint.getB();

            // Next Connection
            if(grid.has(x + 1, z) && grid.getCell(x + 1, z) == GridCellType.Connection){
                Wire current = new Wire();
                current.setTerminalA(grid.localToBlockPos(x, z));
                List<PositionTuple> had = new ArrayList<>();
                had.add(PositionTuple.of(x,z));
                out.addAll(traceWire(x, z, had, grid, current));
            }
            if(grid.has(x - 1, z) && grid.getCell(x - 1, z) == GridCellType.Connection){
                Wire current = new Wire();
                current.setTerminalA(grid.localToBlockPos(x, z));
                List<PositionTuple> had = new ArrayList<>();
                had.add(PositionTuple.of(x,z));
                out.addAll(traceWire(x, z, had, grid, current));
            }
            if(grid.has(x ,z + 1) && grid.getCell(x, z + 1) == GridCellType.Connection){
                Wire current = new Wire();
                current.setTerminalA(grid.localToBlockPos(x, z));
                List<PositionTuple> had = new ArrayList<>();
                had.add(PositionTuple.of(x,z));
                out.addAll(traceWire(x, z, had, grid, current));
            }
            if(grid.has(x, z - 1) && grid.getCell(x, z - 1) == GridCellType.Connection){
                Wire current = new Wire();
                current.setTerminalA(grid.localToBlockPos(x, z));
                List<PositionTuple> had = new ArrayList<>();
                had.add(PositionTuple.of(x,z));
                out.addAll(traceWire(x, z, had, grid, current));
            }
        }
        return out;
    }

    public static HashSet<Wire> traceWire(int x, int z, List<PositionTuple> had, Grid grid, Wire wire){

        HashSet<Wire> out = new HashSet<>();

        if(!had.contains(PositionTuple.of(x + 1, z)) && grid.has(x + 1, z) && grid.getCell(x + 1, z) == GridCellType.NetworkMember){
            wire.setTerminalB(grid.localToBlockPos(x + 1, z));
            out.add(wire);
        }
        if(!had.contains(PositionTuple.of(x - 1, z)) && grid.has(x - 1, z) && grid.getCell(x - 1, z) == GridCellType.NetworkMember){
            wire.setTerminalB(grid.localToBlockPos(x - 1, z));
            out.add(wire);
        }
        if(!had.contains(PositionTuple.of(x, z + 1)) && grid.has(x ,z + 1) && grid.getCell(x, z + 1) == GridCellType.NetworkMember){
            wire.setTerminalB(grid.localToBlockPos(x, z + 1));
            out.add(wire);
        }
        if(!had.contains(PositionTuple.of(x, z - 1)) && grid.has(x, z - 1) && grid.getCell(x, z - 1) == GridCellType.NetworkMember){
            wire.setTerminalB(grid.localToBlockPos(x, z - 1));
            out.add(wire);
        }


        // Next Connection
        if(!had.contains(PositionTuple.of(x + 1, z)) && grid.has(x + 1, z) && grid.getCell(x + 1, z) == GridCellType.Connection){
            wire.appendBlock(grid.localToBlockPos(x+1, z), grid.getBlockStateAt(x+1, z));
            had.add(PositionTuple.of(x + 1, z));
            out.addAll(traceWire(x + 1, z, had, grid, wire));
        }
        if(!had.contains(PositionTuple.of(x - 1, z)) && grid.has(x - 1, z) && grid.getCell(x - 1, z) == GridCellType.Connection){
            wire.appendBlock(grid.localToBlockPos(x-1, z), grid.getBlockStateAt(x-1, z));
            had.add(PositionTuple.of(x - 1, z));
            out.addAll(traceWire(x - 1, z, had, grid, wire));
        }
        if(!had.contains(PositionTuple.of(x, z + 1)) && grid.has(x ,z + 1) && grid.getCell(x, z + 1) == GridCellType.Connection){
            wire.appendBlock(grid.localToBlockPos(x, z + 1), grid.getBlockStateAt(x, z + 1));
            had.add(PositionTuple.of(x, z + 1));
            out.addAll(traceWire(x, z + 1, had, grid, wire));
        }
        if(!had.contains(PositionTuple.of(x, z - 1)) && grid.has(x, z - 1) && grid.getCell(x, z - 1) == GridCellType.Connection){
            wire.appendBlock(grid.localToBlockPos(x, z - 1), grid.getBlockStateAt(x, z - 1));
            had.add(PositionTuple.of(x, z - 1));
            out.addAll(traceWire(x, z - 1, had, grid, wire));
        }

        return out;
    }

    public static List<Tuple<Integer, Integer>> scanGridForMembersInternal(Grid grid){
        List<Tuple<Integer, Integer>> out = new ArrayList<>();
        for(int x = -grid.size(); x < grid.size() + 1; x++){
            for(int z = -grid.size(); z < grid.size() + 1; z++) {
                if(grid.getCell(x, z) == GridCellType.NetworkMember){
                    out.add(new Tuple<>(x, z));
                }
            }
        }
        return out;
    }


    public static List<BlockPos> scanGridForMembers(Grid grid) {
        return scanGridForMembersInternal(grid).stream()
                .map(cord -> grid.localToBlockPos(cord.getA(), cord.getB()))
                .toList();
    }
}

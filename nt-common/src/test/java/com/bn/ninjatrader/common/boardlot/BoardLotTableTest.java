package com.bn.ninjatrader.common.boardlot;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/18/16.
 */
public class BoardLotTableTest {

  private final BoardLotTable table = new BoardLotTable();

  @Test
  public void testGetBoardLot() {
    BoardLot boardLot = table.getBoardLot(0.0001);
    assertBoardLotTickAndLot(boardLot, 0.0001, 1000000);

    boardLot = table.getBoardLot(0.0099);
    assertBoardLotTickAndLot(boardLot, 0.0001, 1000000);

    boardLot = table.getBoardLot(0.011);
    assertBoardLotTickAndLot(boardLot, 0.0010, 100000);

    boardLot = table.getBoardLot(0.0499);
    assertBoardLotTickAndLot(boardLot, 0.0010, 100000);

    boardLot = table.getBoardLot(5.2);
    assertBoardLotTickAndLot(boardLot, 0.01, 100);

    boardLot = table.getBoardLot(100);
    assertBoardLotTickAndLot(boardLot, 0.1, 10);

    boardLot = table.getBoardLot(4999);
    assertBoardLotTickAndLot(boardLot, 2, 5);

    boardLot = table.getBoardLot(50000);
    assertBoardLotTickAndLot(boardLot, 5, 5);
  }

  private void assertBoardLotTickAndLot(BoardLot boardLot, double tick, int lotSize) {
    assertEquals(boardLot.getTick(), tick);
    assertEquals(boardLot.getLotSize(), lotSize);
  }
}

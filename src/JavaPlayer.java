import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * version:1.0
 */
public class JavaPlayer {
    int time;//每步时间
    int hold;//所持黑子或白子
    int sum = 0;//步数
    //权值表
    int[] weightIndicators = {100, -5, 10, 5, 5, 10, -5, 100,
            -5, -45, 1, 1, 1, 1, -45, -5,
            10, 1, 3, 2, 2, 3, 1, 10,
            5, 1, 2, 1, 1, 2, 1, 5,
            5, 1, 2, 1, 1, 2, 1, 5,
            10, 1, 3, 2, 2, 3, 1, 10,
            -5, -45, 1, 1, 1, 1, -45, -5,
            100, -5, 10, 5, 5, 10, -5, 100
    };

    public void ready(int no, int timeLimit) throws Exception {
        time = timeLimit;
        hold = no;
    }

    public int run(int[] board) throws Exception {
        sum++;
        System.out.println("步数：" + sum + "");
        List<Map<String, Integer>> canWalkList = new ArrayList<Map<String, Integer>>();
        laoziMethod(board, hold == 1 ? 2 : 1, hold == 1 ? 1 : 2, canWalkList);
        System.out.println("可落子位置:" + canWalkList);

        //canWalkList,根据伴随度进行降序
//        List<WeightedObject> collect = canWalkList.stream()
//                .sorted(Comparator.comparing(WeightedObject::getWeight).reversed())
//                .collect(Collectors.toList());


//        System.out.println("降序排序后的落子位置：" + collect);
        int i = compareValue(canWalkList);
        System.out.println("权值最大的落子位置：" + i);
        return i;
    }

    public boolean judgmentTool(int param) {
        return param >= 0 && param < 64 ? true : false;
    }

    /**
     * 把位置与权值放入map中
     *
     * @param coordinate 坐标
     * @param weight     权值
     */
    public Map newMap(int coordinate, int weight) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("coordinate", coordinate);
        hashMap.put("weight", weight);
        return hashMap;
    }

    /**
     * 取出权值最大的元素
     *
     * @param list 比较数组
     */
    public int compareValue(List<Map<String, Integer>> list) {
        int maxCoordinate = -1;
        int maxWeight = -100;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("weight") > maxWeight) {
                maxWeight = list.get(i).get("weight");
                maxCoordinate = list.get(i).get("coordinate");
            }
        }
        return maxCoordinate;
    }

    /**
     * 判断多子的位置是否可下
     *
     * @param board              棋盘
     * @param hold               自己所持棋子颜色
     * @param opponent           对手所持棋子颜色
     * @param list               可落子位置list
     * @param superpositionValue 叠加值
     * @param offset             偏移量
     * @param i                  当前循环位置
     */
    public void laoziMoreMethod1(int[] board, int opponent, int hold, List list, int superpositionValue, int offset, int i) {
        if (judgmentTool(i + 1) && judgmentTool(i - 1)) {
            int rounding = (i + 1) / 8;//取整
            int residual = (i + 1) % 8;//取余
            if (residual > 1 && board[i - 1] == 0) {
                list.add(newMap(i - 1, weightIndicators[i - 1]));
            }
            if (residual > 1 && board[i - 1] == opponent) {
                laoziMoreMethod1(board, opponent, hold, list, superpositionValue, offset, i + superpositionValue + offset);
            }
        }
    }

    public void laoziMoreMethod2(int[] board, int opponent, int hold, List list, int superpositionValue, int offset, int i) {
        if (judgmentTool(i + 1) && judgmentTool(i - 1)) {
            int rounding = (i + 1) / 8;//取整
            int residual = (i + 1) % 8;//取余
            if (residual > 0 && board[i + 1] == 0) {
                list.add(newMap(i + 1, weightIndicators[i + 1]));
            }
            if (residual > 0 && board[i + 1] == opponent) {
                laoziMoreMethod2(board, opponent, hold, list, superpositionValue, offset, i + superpositionValue + offset);
            }
        }
    }

    public void laoziMoreMethod3(int[] board, int opponent, int hold, List list, int superpositionValue, int offset, int i) {
        if (judgmentTool(i + 1) && judgmentTool(i - 8)) {
            int rounding = (i + 1) / 8;//取整
            int residual = (i + 1) % 8;//取余
            if (rounding > 0 && board[i - 8] == 0) {
                list.add(newMap(i - 8, weightIndicators[i - 8]));
            }
            if (rounding > 0 && board[i - 8] == opponent) {
                laoziMoreMethod3(board, opponent, hold, list, superpositionValue, offset, i + superpositionValue + offset);
            }
        }
    }

    public void laoziMoreMethod4(int[] board, int opponent, int hold, List list, int superpositionValue, int offset, int i) {
        if (judgmentTool(i + 1) && judgmentTool(i + 8)) {
            int rounding = (i + 1) / 8;//取整
            int residual = (i + 1) % 8;//取余
            if (rounding < 8 && board[i + 8] == 0) {
                list.add(newMap(i + 8, weightIndicators[i + 8]));
            }
            if (rounding < 8 && board[i + 8] == opponent) {
                laoziMoreMethod4(board, opponent, hold, list, superpositionValue, offset, i + superpositionValue + offset);
            }
        }
    }

    public void laoziMoreMethod5(int[] board, int opponent, int hold, List list, int superpositionValue, int offset, int i) {
        if (judgmentTool(i + 1) && judgmentTool(i - 7)) {
            int rounding = (i + 1) / 8;//取整
            int residual = (i + 1) % 8;//取余
            if (rounding > 0 && residual > 0 && board[i - 7] == 0) {
                list.add(newMap(i - 7, weightIndicators[i - 7]));
            }
            if (rounding > 0 && residual > 0 && board[i - 7] == opponent) {
                laoziMoreMethod5(board, opponent, hold, list, superpositionValue, offset, i + superpositionValue + offset);
            }
        }
    }

    public void laoziMoreMethod6(int[] board, int opponent, int hold, List list, int superpositionValue, int offset, int i) {
        if (judgmentTool(i + 1) && judgmentTool(i + 7)) {
            int rounding = (i + 1) / 8;//取整
            int residual = (i + 1) % 8;//取余
            if (rounding < 8 && residual > 1 && board[i + 7] == 0) {
                list.add(newMap(i + 7, weightIndicators[i + 7]));
            }
            if (rounding < 8 && residual > 1 && board[i + 7] == opponent) {
                laoziMoreMethod6(board, opponent, hold, list, superpositionValue, offset, i + superpositionValue + offset);
            }
        }
    }

    public void laoziMoreMethod7(int[] board, int opponent, int hold, List list, int superpositionValue, int offset, int i) {
        if (judgmentTool(i + 1) && judgmentTool(i - 9)) {
            int rounding = (i + 1) / 8;//取整
            int residual = (i + 1) % 8;//取余
            if (rounding > 0 && residual > 1 && board[i - 9] == 0) {
                list.add(newMap(i - 9, weightIndicators[i - 9]));
            }
            if (rounding > 0 && residual > 1 && board[i - 9] == opponent) {
                laoziMoreMethod7(board, opponent, hold, list, superpositionValue, offset, i + superpositionValue + offset);
            }
        }
    }

    public void laoziMoreMethod8(int[] board, int opponent, int hold, List list, int superpositionValue, int offset, int i) {
        if (judgmentTool(i + 1) && judgmentTool(i + 9)) {
            int rounding = (i + 1) / 8;//取整
            int residual = (i + 1) % 8;//取余
            if (rounding < 8 && residual > 0 && board[i + 9] == 0) {
                list.add(newMap(i + 9, weightIndicators[i + 9]));
            }
            if (rounding < 8 && residual > 0 && board[i + 9] == opponent) {
                laoziMoreMethod8(board, opponent, hold, list, superpositionValue, offset, i + superpositionValue + offset);
            }
        }
    }

    /**
     * 根据对手棋子判断落子位置
     *
     * @param board    棋盘
     * @param hold     自己所持棋子颜色
     * @param opponent 对手所持棋子颜色
     * @param list     可落子位置list
     */
    public void laoziMethod(int[] board, int opponent, int hold, List list) {
        for (int i = 0; i < 64; i++) {
            if (board[i] == opponent) {
                int rounding = (i + 1) / 8;//取整
                int residual = (i + 1) % 8;//取余
                System.out.println(rounding + "-----" + residual);
                if (judgmentTool(i + 1) && residual > 0 && board[i + 1] == hold) {
                    /* if (residual > 1 && judgmentTool(i - 1) && board[i - 1] == 0) {
                        list.add(newMap(i - 1, weightIndicators[i - 1]));
                    }*/
                    laoziMoreMethod1(board, opponent, hold, list, -1, 0, i);
                }
                if (judgmentTool(i - 1) && residual > 1 && board[i - 1] == hold) {
                    /* if (residual > 0 && judgmentTool(i + 1) && board[i + 1] == 0) {
                        list.add(newMap(i + 1, weightIndicators[i + 1]));
                    }*/
                    laoziMoreMethod2(board, opponent, hold, list, +1, 0, i);
                }
                if (judgmentTool(i + 8) && rounding < 8 && board[i + 8] == hold) {
                    /*if (rounding > 0 && judgmentTool(i - 8) && board[i - 8] == 0) {
                        list.add(newMap(i - 8, weightIndicators[i - 8]));
                    }*/
                    laoziMoreMethod3(board, opponent, hold, list, -8, 0, i);
                }
                if (judgmentTool(i - 8) && rounding > 0 && board[i - 8] == hold) {
                    /*if (rounding < 8 && judgmentTool(i + 8) && board[i + 8] == 0) {
                        list.add(newMap(i + 8, weightIndicators[i + 8]));
                    }*/
                    laoziMoreMethod4(board, opponent, hold, list, +8, 0, i);
                }
                if (judgmentTool(i + 7) && rounding < 8 && residual > 1 && board[i + 7] == hold) {
                    /*if (rounding > 0 && residual > 0 && judgmentTool(i - 7) && board[i - 7] == 0) {
                        list.add(newMap(i - 7, weightIndicators[i - 7]));
                    }*/
                    laoziMoreMethod5(board, opponent, hold, list, -7, 0, i);
                }
                if (judgmentTool(i - 7) && rounding > 0 && residual > 0 && board[i - 7] == hold) {
                    /*if (rounding < 8 && residual > 1 && judgmentTool(i + 7) && board[i + 7] == 0) {
                        list.add(newMap(i + 7, weightIndicators[i + 7]));
                    }*/
                    laoziMoreMethod6(board, opponent, hold, list, +7, 0, i);
                }
                if (judgmentTool(i + 9) && rounding < 8 && residual > 0 && board[i + 9] == hold) {
                    /*if (rounding > 0 && residual > 1 && judgmentTool(i - 9) && board[i - 9] == 0) {
                        list.add(newMap(i - 9, weightIndicators[i - 9]));
                    }*/
                    laoziMoreMethod7(board, opponent, hold, list, -9, 0, i);
                }
                if (judgmentTool(i - 9) && rounding > 0 && residual > 1 && board[i - 9] == hold) {
                    /*if (rounding < 8 && residual > 0 && judgmentTool(i + 9) && board[i + 9] == 0) {
                        list.add(newMap(i + 9, weightIndicators[i + 9]));
                    }*/
                    laoziMoreMethod8(board, opponent, hold, list, +9, 0, i);
                }
            }
        }
        list.add(newMap(-1, -100));
    }

    public static void main(String[] args) throws Exception {
        JavaPlayer javaPlayer = new JavaPlayer();
        javaPlayer.ready(1, 10);
        int[] init = new int[64];
        for (int i = 0; i < 64; i++) {
            if (i == 27 || i == 36) {
                init[i] = 1;
            } else if (i == 28 || i == 35) {
                init[i] = 2;
            } else {
                init[i] = 0;
            }
        }
        System.out.println(javaPlayer.run(init));
    }
}
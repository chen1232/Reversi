import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        List<WeightedObject> canWalkList = new ArrayList<WeightedObject>();
        laoziMethod(board, hold == 1 ? 2 : 1, hold, canWalkList);
        System.out.println("可落子位置:" + canWalkList);

        //canWalkList,根据伴随度进行降序
        List<WeightedObject> collect = canWalkList.stream()
                .sorted(Comparator.comparing(WeightedObject::getWeight).reversed())
                .collect(Collectors.toList());

        System.out.println("降序排序后的落子位置：" + collect);
        return collect.get(0).getCoordinate();
    }

    public boolean judgmentTool(int param) {
        return param >= 0 && param < 64 ? true : false;
    }

    /**
     * 根据对手棋子判断落子位置
     * @param board    棋盘
     * @param hold     自己所持棋子颜色
     * @param opponent 对手所持棋子颜色
     */
    public void laoziMethod(int[] board, int opponent, int hold, List list) {
        for (int i = 0; i < 64; i++) {
            if (board[i] == opponent) {
                int rounding = (i + 1) / 8;//取整
                int residual = (i + 1) % 8;//取余
                System.out.println(rounding + "-----" + residual);
                if (judgmentTool(i + 1) && residual > 0 && board[i + 1] == hold) {
                    if (residual > 1 && judgmentTool(i - 1) && board[i - 1] == 0) {
                        list.add(new WeightedObject(i - 1, weightIndicators[i - 1]));
                    }
                }
                if (judgmentTool(i - 1) && residual > 1 && board[i - 1] == hold) {
                    if (residual > 0 && judgmentTool(i + 1) && board[i + 1] == 0) {
                        list.add(new WeightedObject(i + 1, weightIndicators[i + 1]));
                    }
                }
                if (judgmentTool(i + 8) && rounding < 8 && board[i + 8] == hold) {
                    if (rounding > 0 && judgmentTool(i - 8) && board[i - 8] == 0) {
                        list.add(new WeightedObject(i - 8, weightIndicators[i - 8]));
                    }
                }
                if (judgmentTool(i - 8) && rounding > 0 && board[i - 8] == hold) {
                    if (rounding < 8 && judgmentTool(i + 8) && board[i + 8] == 0) {
                        list.add(new WeightedObject(i + 8, weightIndicators[i + 8]));
                    }
                }
                if (judgmentTool(i + 7) && rounding < 8 && residual > 1 && board[i + 7] == hold) {
                    if (rounding > 0 && residual > 0 && judgmentTool(i - 7) && board[i - 7] == 0) {
                        list.add(new WeightedObject(i - 7, weightIndicators[i - 7]));
                    }
                }
                if (judgmentTool(i - 7) && rounding > 0 && residual > 0 && board[i - 7] == hold) {
                    if (rounding < 8 && residual > 1 && judgmentTool(i + 7) && board[i + 7] == 0) {
                        list.add(new WeightedObject(i + 7, weightIndicators[i + 7]));
                    }
                }
                if (judgmentTool(i + 9) && rounding < 8 && residual > 0 && board[i + 9] == hold) {
                    if (rounding > 0 && residual > 1 && judgmentTool(i - 9) && board[i - 9] == 0) {
                        list.add(new WeightedObject(i - 9, weightIndicators[i - 9]));
                    }
                }
                if (judgmentTool(i - 9) && rounding > 0 && residual > 1 && board[i - 9] == hold) {
                    if (rounding < 8 && residual > 0 && judgmentTool(i + 9) && board[i + 9] == 0) {
                        list.add(new WeightedObject(i + 9, weightIndicators[i + 9]));
                    }
                }
            }
        }
        list.add(new WeightedObject(-1, -100));
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
//        System.out.println(1 % 8);
    }
}

class WeightedObject {
    /**
     * 坐标
     */
    private int coordinate;

    /**
     * 权值
     */
    private int weight;

    public WeightedObject(int coordinate, int weight) {
        this.coordinate = coordinate;
        this.weight = weight;
    }

    public int getCoordinate() {
        return coordinate;
    }

    public int getWeight() {
        return weight;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "WeightedObject{" +
                "coordinate=" + coordinate +
                ", weight=" + weight +
                '}';
    }
}

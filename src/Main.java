import entity.Goods;
import utils.WarningUtil;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        int[] floorWeight = new int[4];
//        floorWeight[0] = 200;
//        floorWeight[1] = 300;
//        floorWeight[2] = 600;
//        floorWeight[3] = 900;
        List<Goods> goodsList = new ArrayList<>();
        for(int i = 0 ; i < 5 ; i ++){
            Goods goods = new Goods();
            goods.setName(i+1 + "");
            goods.setNum(10);
            goods.setPrice(new BigDecimal(i * 10 + 5));
            goods.setWeight( 100 - i * 7);
            goods.setInaccuracy(3+i);
            goods.setNo(i);
            goodsList.add(goods);
        }
        WarningUtil warningUtil = new WarningUtil();
        warningUtil.setLimitTime(10000);
        warningUtil.warningResult(goodsList);
        List<List<Goods>> lists = warningUtil.getLists();
        List<Goods> goods = warningUtil.getMatchGoods();
        System.out.println("*******************************************************************");
        System.out.println("*****************              系统提示              ****************");
        System.out.println("***************    本系统仅计算单物品和多物品件存在的冲突   **************");
        System.out.println("***************   出错组数最多截止 " + 10000 + " 组，超过部分不计算   **************");
        System.out.println("********************************************************************");
        System.out.println("-------------------------------------------------------------------------");
        LocalDateTime beginTime = LocalDateTime.now();
        for(int i = 0 ; i < lists.size() ; i++){
            System.out.println(goods.get(i).getName() + " " + goods.get(i).getNum() + "件，总重 " + goods.get(i).getWeight() * goods.get(i).getNum()
                    + "g， 最大模糊值为：" + goods.get(i).getMaxWeight() * goods.get(i).getNum()
                    + "g， 最小模糊值为：" + goods.get(i).getMinWeight() * goods.get(i).getNum()
                    + "g， 组合冲突");
            Iterator iterator = lists.get(i).iterator();
            System.out.println("组合详情：");
            while (iterator.hasNext()){
                Goods constitude = (Goods)iterator.next();
                System.out.println(constitude.getName() + " " + constitude.getNum() + " 件 "
                        + "， 最大模糊值为：" + constitude.getMaxWeight() * constitude.getNum()
                        + "g， 最小模糊值为：" + constitude.getMinWeight() * constitude.getNum()
                        + "g ");
            }
            System.out.println("-------------------------------------------------------------------------");
        }
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(beginTime, endTime);
        System.out.println("总冲突数：" + goods.size());
        System.out.println("总用时：" + duration);
    }
}

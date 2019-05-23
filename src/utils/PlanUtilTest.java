package utils;


import entity.Floor;
import entity.Goods;
import sun.plugin2.message.GetAppletMessage;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Classname PlanUtilTest
 * @Description TODO
 * @Date 2019-05-20
 * @Created by chen_bq
 */
public class PlanUtilTest {

    public static void main(String[] args) {
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
        System.out.println("* * * * *      原始商品      * * * * *");
        printGoods(goodsList);
        List<Floor> originFloor = getOriginFloors(goodsList, 4);
        List<Floor> afterFloor = subList(originFloor);
        /*模拟取走物品*/
        Goods goods = goodsList.get(0).cloneGoods();
        goods.setNum(2);
        afterFloor.get(0).removeGoods(goods);
        goods = goodsList.get(3).cloneGoods();
        goods.setNum(1);
        afterFloor.get(1).removeGoods(goods);
        goods = goodsList.get(4).cloneGoods();
        goods.setNum(1);
        afterFloor.get(2).removeGoods(goods);
        /*模拟取走物品*/
        PlanUtil planUtil = new PlanUtil();
        planUtil.calculate(originFloor, afterFloor, goodsList);

        System.out.println("*******************************************************************");
        System.out.println("*****************              系统提示              ****************");
        System.out.println("***************    本系统模拟取物后质量转化商品数量的结果   **************");
        System.out.println("***************   暂不计算超过托盘上限值及托盘自带的误差模糊   **************");
        System.out.println("********************************************************************");
        System.out.println("-------------------------------------------------------------------------");

        StringBuilder stringBuilder = planUtil.getStringBuilder();
        System.out.println(stringBuilder.toString());
        List<List<Goods>> lists = planUtil.getLists();
        System.out.println("* * * * *      变化后商品      * * * * *");
        System.out.println("方案数 ： " +  lists.size());
        for (List<Goods> list : lists ){
            System.out.println("* * * * * * * * * * * * * * * * * * *");
            printGoods(list);
        }
    }

    public static List<Floor> subList(List<Floor> floorsList){
        List<Floor> floors = new ArrayList<>();
        for(int i = 0 ; i < floorsList.size() ; i++){
            floors.add(floorsList.get(i).cloneFloor());
        }
        return floors;
    }

    public static void printGoods(List<Goods> list){
        for(int i = 0 ; i < list.size() ; i++){
            System.out.println(list.get(i).getName() + " " + list.get(i).getNum() + " 件 "
            + " 标准重量： " + list.get(i).getWeight() * list.get(i).getNum()
            + "g, 最大模糊重量： " + list.get(i).getMaxWeight() * list.get(i).getNum()
            + "g, 最小模糊重量： " + list.get(i).getMinWeight() * list.get(i).getNum());
        }
    }

    public static List<Floor> getOriginFloors(List<Goods> goodsList, int floorNum){
        List<Floor> floors = new ArrayList<>();
        int goodsListIndex = goodsList.size() / floorNum;
        for (int i = 0 ; i < floorNum ; i++){
            Floor floor = new Floor();
            int weight = 0;
            if(i != floorNum - 1){
                for(int j = goodsListIndex * i ; j < goodsListIndex * (i+1) ; j++){
                    weight += goodsList.get(j).getWeight() * goodsList.get(j).getNum();
                }
            }else{
                for(int j = goodsListIndex * i ; j < goodsList.size() ; j++){
                    weight += goodsList.get(j).getWeight() * goodsList.get(j).getNum();
                }
            }
            floor.setFloorWeight(weight);
            floor.setInaccuracy(5);
            floors.add(floor);
        }
        return floors;
    }

}

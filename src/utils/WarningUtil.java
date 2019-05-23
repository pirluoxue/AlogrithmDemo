package utils;

import com.sun.org.apache.bcel.internal.classfile.ConstantInteger;
import entity.Goods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Classname WarningUtil
 * @Description 预警算法
 * @Date 2019-05-18
 * @Created by chen_bq
 */
public class WarningUtil {

    private int limitTime = 10;
    private int warningTimes = 0;
    private StringBuilder warningResult = new StringBuilder();
    private List<List<Goods>> lists = new ArrayList<>();
    private List<Goods> matchGoods = new ArrayList<>();

    public void warningResult(List<Goods> goodsList){

        for(int i = 0 ; i < goodsList.size() ; i++){
             calculateGoodsCoupling(goodsList.get(i), goodsList);
        }
    }

    private void calculateGoodsCoupling(Goods calcGoods, List<Goods> goodsList){
        int num = calcGoods.getNum();
        List<Goods> removeCalculatedGoods = new ArrayList<>();
        for(int i = 0 ; i < goodsList.size() ; i++){
            if (calcGoods.getNo() != goodsList.get(i).getNo()){
                removeCalculatedGoods.add(goodsList.get(i));
            }
        }
        for(int i = 1 ; i < num ;i++){
            if(lists != null && lists.size() >= limitTime){
                return;
            }
            calculeteSomethingGoodsConstitute(calcGoods, i, removeCalculatedGoods);
        }
    }

    private void calculeteSomethingGoodsConstitute(Goods calcGoods, int goodsNum, List<Goods> calculatedGoods){
        Goods goods = calcGoods.cloneGoods();
        goods.setNum(goodsNum);
        List<Goods> constitudeList = new ArrayList<>();
        int weight = calcGoods.getWeight() * goodsNum;
        int inaccuracy = calcGoods.getInaccuracy() * goodsNum;
        int maxLimitWeight = weight + inaccuracy;
        int minLimitWeight = weight - inaccuracy;
        for(int i = 0  ; i < calculatedGoods.size() ; i++){
            if(calculatedGoods.get(i).getWeight() < maxLimitWeight){
                if(lists != null && lists.size() >= limitTime){
                    return;
                }
                searchLight(goods, calculatedGoods, constitudeList, i);
            }

        }
    }

    private void searchLight(Goods calcGoods, List<Goods> calculatedGoods
            , List<Goods> consultedGoods, int index){
        if(lists != null && lists.size() >= limitTime){
            return;
        }
        int maxLimitWeight = calcGoods.getMaxWeight() * calcGoods.getNum();
        int minLimitWeight = calcGoods.getMinWeight() * calcGoods.getNum();
        int maxconsultedListWeight = 0;
        int minconsultedListWeight = 0;
        if(consultedGoods != null && consultedGoods.size() > 0){
            for (int i = 0 ; i < consultedGoods.size() ; i++){
                maxconsultedListWeight += consultedGoods.get(i).getMaxWeight() * consultedGoods.get(i).getNum();
                minconsultedListWeight += consultedGoods.get(i).getMinWeight() * consultedGoods.get(i).getNum();
            }
        }
        Goods nowconsultGoods = calculatedGoods.get(index);
        for (int i = 1 ; i < nowconsultGoods.getNum() ; i++){
            if(lists != null && lists.size() >= limitTime){
                return;
            }
            int maxWeight = maxconsultedListWeight + i * nowconsultGoods.getMaxWeight();
            int minWeight = minconsultedListWeight + i * nowconsultGoods.getMinWeight();
            if(maxWeight >= minLimitWeight && minWeight <= maxLimitWeight){
                //匹配则加入list
                Goods goods = nowconsultGoods.cloneGoods();
                goods.setNum(i);
                List<Goods> goodsList = new ArrayList<>();
                Iterator iterator = consultedGoods.iterator();
                while (iterator.hasNext()){
                    goodsList.add((Goods) iterator.next());
                }
                updateConsultedGoods(goods, goodsList);
                lists.add(goodsList);
                matchGoods.add(calcGoods);
                clearLightGoods(goods, consultedGoods);
                break;
                //未超过匹配最大值，递归找更轻的物品
            }else if(index < calculatedGoods.size() - 1 && minWeight < maxLimitWeight){
                Goods goods = nowconsultGoods.cloneGoods();
                goods.setNum(i);
                updateConsultedGoods(goods, consultedGoods);
                searchLight(calcGoods, calculatedGoods, consultedGoods, index + 1);
            }/*else if(i == nowconsultGoods.getNum() -1 ){
                clearLightGoods(nowconsultGoods, consultedGoods);
            }*/
        }
        clearLightGoods(nowconsultGoods, consultedGoods);

    }

    private void updateConsultedGoods(Goods goods, List<Goods> consultedGoods){
        if(consultedGoods != null && consultedGoods.size() == 0){
            consultedGoods.add(goods);
            return ;
        }
        for(int i = 0 ; i < consultedGoods.size() ; i++){
            //去重
            if(consultedGoods.get(i).getNo() == goods.getNo()){
                consultedGoods.remove(i);
                consultedGoods.add(goods);
                break;
            }else if(i == consultedGoods.size() - 1 ){
                consultedGoods.add(goods);
                break;
            }
        }
    }


    private void clearLightGoods(Goods goods, List<Goods> consultedGoods){
        for(int i = 0 ; i < consultedGoods.size() ; i++){
            if(goods.getNo() == consultedGoods.get(i).getNo()){
                consultedGoods.remove(i);
                break;
            }
        }
    }

    public List<List<Goods>> getLists() {
        return lists;
    }

    public List<Goods> getMatchGoods() {
        return matchGoods;
    }

    public void setLimitTime(int limitTime) {
        this.limitTime = limitTime;
    }
}

package utils;

import entity.Floor;
import entity.Goods;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname Plan
 * @Description 规划算法
 * @Date 2019-05-16
 * @Created by chen_bq
 */
public class PlanUtil {

    private StringBuilder stringBuilder = new StringBuilder();
    private List<List<Goods>> lists = new ArrayList<>();

    //必须默认从重到轻排序的商品列表
    //默认不存在倍重问题
    public void calculate(List<Floor> originFloors, List<Floor> afterFloor, List<Goods> originGoodsList){
        int weightVariation = getWeightVariation(originFloors, afterFloor);
        stringBuilder.append("设备前后重量相差： " + weightVariation + " g");
        if(!checkInaccuracy(originFloors, weightVariation)){
            analyMinusWeight(weightVariation, originGoodsList);
        }

    }

    /**
     * 根据前后层重获得总重变化值，默认不存在倍重问题
     * @param floors
     * @param floorAfter
     * @return
     */
    private int getWeightVariation(List<Floor> floors, List<Floor> floorAfter){
        int weightVariation = 0;
        for(int i =0 ; i < floors.size() ; i++){
            weightVariation += (floors.get(i).getFloorWeight() - floorAfter.get(i).getFloorWeight());
        }
        return weightVariation;
    }

    /**
     * 校验是否由于误差引起的重量变化
     * @param floors
     * @param weightVariation
     * @return
     */
    private boolean checkInaccuracy(List<Floor> floors, int weightVariation){
        int inaccuracy = 0;
        for(int i = 0 ; i <floors.size() ; i++){
            inaccuracy += floors.get(i).getInaccuracy();
        }
        if(weightVariation < inaccuracy && weightVariation > -inaccuracy){
            return true;
        }else{
            return false;
        }
    }

    //分析差值重量匹配结果
    private void analyMinusWeight(int weightVariation, List<Goods> originGoodsList){
        List<Goods> preliminaryGoods = new ArrayList<>();
        //初步筛选，过滤重量已经大于模糊上限值的商品
        for(int i = 0 ; i < originGoodsList.size() ; i++){
            int maxWeight = originGoodsList.get(i).getMaxWeight();
            int minWeight = originGoodsList.get(i).getMinWeight();
            if(weightVariation > maxWeight){
                preliminaryGoods.add(originGoodsList.get(i));
            }
        }
        searchLighter(weightVariation, preliminaryGoods, new ArrayList<>(), 0);
    }

    private void searchLighter(int weightVariation, List<Goods> preliminaryGoods
            , List<Goods> consultedGoods, int goodsIndex){
        Goods goods = preliminaryGoods.get(goodsIndex).cloneGoods();
        int goodsTotalNum = goods.getNum();
        if(consultedGoods == null || consultedGoods.size() == 0){
            goods.setNum(0);
            consultedGoods.add(goods);
        }
        int maxConsultedWeight = 0;
        int minConsultedWeight = 0;
        for(int i = 0 ; i < consultedGoods.size() ; i++){
            maxConsultedWeight += consultedGoods.get(i).getMaxWeight() * consultedGoods.get(i).getNum();
            minConsultedWeight += consultedGoods.get(i).getMinWeight() * consultedGoods.get(i).getNum();
        }
        for(int i = 0 ; i < goodsTotalNum ; i++){
            int maxWeight = maxConsultedWeight + goods.getMaxWeight() * i;
            int minWeight = minConsultedWeight + goods.getMinWeight() * i;
            //匹配则记录信息
            if(maxWeight >= weightVariation && minWeight <= weightVariation){
                goods.setNum(i);
                updateGoodsList(consultedGoods, goods);
                List<Goods> goodsList = cloneGoodsList(consultedGoods);
                clearZeroGoods(goodsList);
                lists.add(goodsList);
                //组合完成清除当前商品，回滚递归
                removeGoods(consultedGoods, goods);
                break;
            }else if(maxWeight < weightVariation){
                //存在更轻的商品，则进行递归
                if(goodsIndex < preliminaryGoods.size() - 2){
                    Goods tmpGoods = goods.cloneGoods();
                    tmpGoods.setNum(i);
                    updateGoodsList(consultedGoods, tmpGoods);
                    searchLighter(weightVariation, preliminaryGoods, consultedGoods, goodsIndex + 1);
                }
            }else{
                //当前参考商品的最小值已经超过实际差值，回滚
                break;
            }
        }
        removeGoods(consultedGoods, goods);
    }

    private void updateGoodsList(List<Goods> goodsList, Goods goods){
        for(int i = 0 ; i < goodsList.size() ; i ++){
            //序列号相同，则替换
            if(goodsList.get(i).getNo() == goods.getNo()){
                goodsList.remove(i);
                goodsList.add(goods);
                break;
            }
            if(i == goodsList.size() - 1){
                goodsList.add(goods);
            }
        }
    }

    private void clearZeroGoods(List<Goods> goodsList){
        for(int i = 0 ; i < goodsList.size() ; i++){
            if(goodsList.get(i).getNum() == 0){
                goodsList.remove(i);
                i--;
            }
        }
    }

    private void removeGoods(List<Goods> goodsList, Goods goods){
        for(int i = 0 ; i < goodsList.size() ; i ++){
            if(goodsList.get(i).getNo() == goods.getNo()){
                goodsList.remove(i);
                break;
            }
        }
    }

    private List<Goods> cloneGoodsList(List<Goods> goodsList){
        List<Goods> goods = new ArrayList<>();
        for(Goods tmp:goodsList){
            goods.add(tmp.cloneGoods());
        }
        return goods;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public List<List<Goods>> getLists() {
        return lists;
    }
}

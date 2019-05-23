package entity;

/**
 * @Classname Floor
 * @Description TODO
 * @Date 2019-05-22
 * @Created by chen_bq
 */
public class Floor implements Cloneable{

    private int inaccuracy;//层间误差值
    private int floorWeight;//当前层重
    private int maxFloorWeight;//当前层重最大值
    private int minFloorWeight;//当前层重最小值
    private int floorLimitWeight;//层重最大限制

    public Floor cloneFloor() {
        try {
            return this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Floor();
    }

    @Override
    protected Floor clone() throws CloneNotSupportedException {
        return (Floor) super.clone();
    }

    public void removeGoods(Goods goods){
        floorWeight -= goods.getNum() * goods.getWeight();
        System.out.println("拿走商品为： " + goods.getName() + " " + goods.getNum() + " 件"
                + " 标准重量： " + goods.getWeight() * goods.getNum()
                + "g, 最大模糊重量： " + goods.getMaxWeight() * goods.getNum()
                + "g, 最小模糊重量： " + goods.getMinWeight() * goods.getNum());
    }

    public int getInaccuracy() {
        return inaccuracy;
    }

    public int getFloorWeight() {
        return floorWeight;
    }

    public int getMaxFloorWeight() {
        return maxFloorWeight;
    }

    public int getMinFloorWeight() {
        return minFloorWeight;
    }

    public void setInaccuracy(int inaccuracy) {
        this.inaccuracy = inaccuracy;
        if(floorWeight != 0){
            this.maxFloorWeight = floorWeight + inaccuracy;
            this.minFloorWeight = floorWeight - inaccuracy;
        }
    }

    public void setFloorWeight(int floorWeight) {
        this.floorWeight = floorWeight;
        if(inaccuracy != 0){
            this.maxFloorWeight = floorWeight + inaccuracy;
            this.minFloorWeight = floorWeight - inaccuracy;
        }
    }
}

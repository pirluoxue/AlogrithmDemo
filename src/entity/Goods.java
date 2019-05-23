package entity;

import java.math.BigDecimal;

/**
 * @Classname Goods
 * @Description TODO
 * @Date 2019-05-16
 * @Created by chen_bq
 */
public class Goods implements Cloneable {

    private int no;//商品编号
    private int num;//商品数量
    private int weight;//商品重量
    private BigDecimal price;//商品金额
    private String name;//商品名称
    private int inaccuracy;//误差值
    private int maxWeight;//误差导致的重量最大值
    private int minWeight;//误差导致的重量最小值

    public Goods cloneGoods() {
        try {
            return this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Goods();
    }

    public int getMinWeight() {
        return minWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    @Override
    protected Goods clone() throws CloneNotSupportedException {
        return (Goods) super.clone();
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNo() {
        return no;
    }

    public void setInaccuracy(int inaccuracy) {
        this.inaccuracy = inaccuracy;
        if (weight != 0){
            maxWeight = weight + inaccuracy;
            minWeight = weight - inaccuracy;
        }
    }

    public int getInaccuracy() {
        return inaccuracy;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        if (inaccuracy != 0){
            maxWeight = weight + inaccuracy;
            minWeight = weight - inaccuracy;
        }
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setName(String name) {
        int nameNo = Integer.parseInt(name);
        switch (nameNo){
            case 1:this.name = "东阿阿胶";break;
            case 2:this.name = "云南白药";break;
            case 3:this.name = "汇仁肾宝";break;
            case 4:this.name = "999感冒灵";break;
            case 5:this.name = "安神补脑液";break;
            case 6:this.name = "二丁颗粒";break;
            case 7:this.name = "肺宁颗粒";break;
            case 8:this.name = "杜蕾斯";break;
            case 9:this.name = "健胃消食片";break;
            case 10:this.name = "斯达舒";break;
        }

    }

    public int getNum() {
        return num;
    }

    public int getWeight() {
        return weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}

package site.tdbr.stock;

/**
 * Created by tdbr on 2017/12/18.
 */

public class StockItem implements Cloneable{
    Item item;
    int stock;
    public StockItem(Item item,int stock){
        this.item=item;
        this.stock=stock;
    }
    public double value(){
        return this.item.price*this.stock;
    }
    public String getValueString(){return StockData.dfDouble.format(this.value());}
    @Override
    protected Object clone() {
        try{
            return super.clone();
        }catch (CloneNotSupportedException e){
            System.err.println(e);
        }
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.tdbr.stock;

/**
 *
 * @author tdbr
 */
public class Item {
    final int id;
    final String name;
    final double price;
    public Item(int id,String name,double price){
        this.id=id;
        this.name=name;
        this.price=price;
    }
    public String getIDString(){
        String result=String.valueOf(this.id);
        switch (result.length()){
            case 1:
                result="00"+result;
                return result;
            case 2:
                result="0"+result;
                return result;
            case 3:
                return result;
        }
        return result;
    }
    public String getPriceString(){
        return StockData.dfDouble.format(this.price);
    }
    @Override
    public String toString(){
        return this.getIDString()+" "+this.name+" "+this.getPriceString();
    }
}

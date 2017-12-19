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
    int id;
    boolean using;
    String name;
    double price;
    int stock;
    public Item(int id,int stock){
        this.id=id;
        this.using=true;
        this.stock=stock;
    }
    public Item(int id,String name,double price){
        this.id=id;
        this.name=name;
        this.price=price;
        this.using=false;
    }
    public double value(){
        return this.price*this.stock;
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
    public String getValueString(){return StockData.dfDouble.format(this.value());}
    @Override
    public String toString(){
        return this.id+" "+this.name+" "+this.price;
    }
}

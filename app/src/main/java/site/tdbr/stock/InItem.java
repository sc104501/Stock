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
public class InItem{
    Item item;
    int inNum;
    double inPrice;
    public InItem(Item item,int inNum,double inPrice){
        this.item=item;
        this.inNum=inNum;
        this.inPrice=inPrice;
    }
    public double cost(){
        return this.inNum*this.inPrice;
    }
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

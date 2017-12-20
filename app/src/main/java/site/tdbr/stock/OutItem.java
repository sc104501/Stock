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
public class OutItem {
    Item item;
    int outNum;
    double outPrice;
    public OutItem(Item item,int outNum,double outPrice){
        this.item=item;
        this.outNum=outNum;
        this.outPrice=outPrice;
    }
    public double get(){
        return this.outPrice*this.outNum;
    }
    public double gain(){
        return (this.outPrice-this.item.price)*this.outNum;
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

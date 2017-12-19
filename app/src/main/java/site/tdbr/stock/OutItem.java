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
    double outPrice;
    int outNum;
    public OutItem(Item item,double outPrice,int outNum){
        this.item=item;
        this.outPrice=outPrice;
        this.outNum=outNum;
    }
    public double get(){
        return this.outPrice*this.outNum;
    }
    public double gain(){
        return (this.outPrice-this.item.price)*this.outNum;
    }
}

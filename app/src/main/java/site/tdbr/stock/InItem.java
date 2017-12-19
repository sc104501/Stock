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
    double inPrice;
    int inNum;
    public InItem(Item item,double price,int inNum){
        this.item=item;
        this.inPrice=price;
        this.inNum=inNum;
    }
    public double cost(){
        return this.inNum*this.inPrice;
    }
}

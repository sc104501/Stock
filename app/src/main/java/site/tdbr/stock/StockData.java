/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.tdbr.stock;
import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author tdbr
 */
public class StockData {

    File all, stock, stockHis, inHis, outHis;
    List<Item> items;
    Map<Integer,StockItem> currentStocks;
    Map<Date, List<StockItem>> stockItemsHis;
    Map<Date, List<InItem>> inItemsHis;
    Map<Date, List<OutItem>> outItemsHis;
    static SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    static DecimalFormat dfDouble = new DecimalFormat("###0.00");

    public StockData(String location) {
        this.all = new File(location + "all.txt");
        this.stock = new File(location + "stock.txt");
        this.stockHis = new File(location + "stockHis.txt");
        this.inHis = new File(location + "inHis.txt");
        this.outHis = new File(location + "outHis.txt");
        items = new ArrayList<>();
        currentStocks =new LinkedHashMap<>();
        stockItemsHis = new LinkedHashMap<>();
        inItemsHis = new LinkedHashMap<>();
        outItemsHis = new LinkedHashMap<>();
    }

    public void init() throws IOException ,ParseException{
        if(!all.exists()){
            all.createNewFile();
        }
        if(!stock.exists()){
            stock.createNewFile();
        }
        if(!stockHis.exists()){
            stockHis.createNewFile();
        }
        if(!inHis.exists()){
            inHis.createNewFile();
        }
        if(!outHis.exists()){
            outHis.createNewFile();
        }
        getData(this.all);
        getData(this.stock);
        getData(this.stockHis);
        getData(this.inHis);
        getData(this.outHis);
    }

    public void getData(File fileName) throws IOException,ParseException{
        List<List<String>> info = StockData.getContent(fileName);
        Date ldt=null;
        String name=(fileName.toString().split("/")[fileName.toString().split("/").length-1]);
        switch (name) {
            case ("all.txt"):
                for (List<String> line : info) {
                    items.add(new Item(
                            Integer.parseInt(line.get(0)),
                            line.get(1),
                            Double.parseDouble(line.get(2))));
                }
                break;
            case ("stock.txt"):
                for (List<String> line : info) {
                    currentStocks.put(Integer.parseInt(line.get(0)),new StockItem(
                            items.get(Integer.parseInt(line.get(0))),
                            Integer.parseInt(line.get(1))));
                }
                break;
            case ("stockHis.txt"):
                for (List<String> line : info) {
                    if (line.size() == 1) {
                        ldt = sdf.parse(line.get(0));
                        stockItemsHis.put(ldt, new ArrayList<StockItem>());
                    } else {
                        Item item = items.get(Integer.parseInt(line.get(0)));
                        StockItem stockItem = new StockItem(item, Integer.parseInt(line.get(1)));
                        stockItemsHis.get(ldt).add(stockItem);
                    }
                }
                break;
            case ("inHis.txt"):
                for (List<String> line : info) {
                    if (line.size() == 1) {
                        ldt = sdf.parse(line.get(0));
                        inItemsHis.put(ldt, new ArrayList<InItem>());
                    } else {
                        Item item = items.get(Integer.parseInt(line.get(0)));
                        InItem ii = new InItem(item,
                                Integer.parseInt(line.get(1)),
                                Double.parseDouble(line.get(2))
                        );
                        inItemsHis.get(ldt).add(ii);
                    }
                }
                break;
            case ("outHis.txt"):
                for (List<String> line : info) {
                    if (line.size() == 1) {
                        ldt = sdf.parse(line.get(0));
                        outItemsHis.put(ldt, new ArrayList<OutItem>());
                    } else {
                        Item item = items.get(Integer.parseInt(line.get(0)));
                        OutItem oi = new OutItem(item,
                                Integer.parseInt(line.get(1)),
                                Double.parseDouble(line.get(2))
                        );
                        outItemsHis.get(ldt).add(oi);
                    }
                }
                break;
        }
    }

    public void saveData(File fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        String name=(fileName.toString().split("/")[fileName.toString().split("/").length-1]);
        switch (name) {
            case ("all.txt"):
                System.err.println("Can not change \"all.txt\"");
                break;
            case ("stock.txt"):
                for (Map.Entry<Integer,StockItem> entry : currentStocks.entrySet()) {
                    lines.add(String.valueOf(entry.getKey()) + ","
                            + String.valueOf(entry.getValue().stock));
                }
                saveContent(this.stock, lines);
                break;
            case ("stockHis.txt"):
                for (Map.Entry<Date, List<StockItem>> entry : stockItemsHis.entrySet()) {
                    Date ldt = entry.getKey();
                    List<StockItem> stockItems = entry.getValue();
                    lines.add(sdf.format(ldt));
                    for (StockItem stockHisItem : stockItems) {
                        lines.add(String.valueOf(stockHisItem.item.id) + "," + stockHisItem.stock);
                    }
                }
                saveContent(this.stockHis, lines);
                break;
            case ("inHis.txt"):
                for (Map.Entry<Date, List<InItem>> entry : inItemsHis.entrySet()) {
                    Date ldt = entry.getKey();
                    List<InItem> inItemsList = entry.getValue();
                    lines.add(sdf.format(ldt));
                    for (InItem inItems : inItemsList) {
                        lines.add(String.valueOf(inItems.item.id) + "," + inItems.inPrice + "," + inItems.inNum);
                    }
                }
                saveContent(this.inHis, lines);
                break;
            case ("outHis.txt"):
                for (Map.Entry<Date, List<OutItem>> entry : outItemsHis.entrySet()) {
                    Date ldt = entry.getKey();
                    List<OutItem> outItemsList = entry.getValue();
                    lines.add(sdf.format(ldt));
                    for (OutItem outItems : outItemsList) {
                        lines.add(String.valueOf(outItems.item.id) + "," + outItems.outPrice + "," + outItems.outNum);
                    }
                }
                saveContent(this.outHis, lines);
                break;
        }
    }

    private static List<List<String>> getContent(File fileName) throws IOException {
        List<List<String>> info = new ArrayList();

        List<String> lines = readAllLines(fileName);
        for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
            String line = iterator.next();
            info.add(java.util.Arrays.asList(line.split(",")));
        }
        return info;
    }

    private static void saveContent(File fileName, List<String> lines) throws IOException {
        write(fileName, lines);
    }

    public static double getStockValue(List<StockItem> stockItems) {
        double sumValue = 0;
        for (StockItem stockItem : stockItems) {
            sumValue += stockItem.item.price * stockItem.stock;
        }
        return sumValue;
    }

    public static int getStockNum(List<StockItem> stockItems) {
        int sumNum = 0;
        for (StockItem stockItem : stockItems) {
                sumNum += stockItem.stock;
        }
        return sumNum;
    }

    //id,inNum,inPrice
    public void in(Map<Integer,List<String>> info) throws IOException {
        Date ldt = Calendar.getInstance().getTime();
        //start backup
        //append stock info
        List<StockItem> stockItems = new ArrayList<>();
        for(StockItem si:stockItems){
            stockItems.add((StockItem) si.clone());
        }
        stockItemsHis.put(ldt, stockItems);
        //append inHis info
        List<InItem> inItems = new ArrayList<>();
        for (Map.Entry<Integer,List<String>> entry: info.entrySet()) {
            inItems.add(new InItem(items.get(entry.getKey()),
                    Integer.parseInt(entry.getValue().get(0)),
                    Double.parseDouble(entry.getValue().get(1))));
        }
        inItemsHis.put(ldt, inItems);
        saveData(this.stockHis);
        saveData(this.inHis);
        //start in
        for (InItem inItem : inItems) {
            StockItem si=currentStocks.get(inItem.item.id);
            si.stock+=inItem.inNum;
        }
        saveData(this.stock);
    }
    //id,outNum,outPrice
    public void out(Map<Integer,List<String>> info) throws IOException {
        Date ldt = Calendar.getInstance().getTime();
        //start backup
        //append stock info
        List<StockItem> stockItems = new ArrayList<>();
        for(StockItem si:stockItems){
            stockItems.add((StockItem) si.clone());
        }
        stockItemsHis.put(ldt, stockItems);
        //append inHis info
        List<OutItem> outItems = new ArrayList<>();
        for (Map.Entry<Integer,List<String>> entry: info.entrySet()) {
            outItems.add(new OutItem(items.get(entry.getKey()),
                    Integer.parseInt(entry.getValue().get(0)),
                    Double.parseDouble(entry.getValue().get(1))));
        }
        outItemsHis.put(ldt, outItems);
        saveData(this.stockHis);
        saveData(this.outHis);
        //start in
        for (OutItem outItem : outItems) {
            StockItem si=currentStocks.get(outItem.item.id);
            si.stock+=outItem.outNum;
        }
        saveData(this.stock);
    }

    public void changeUsing(List<Boolean> usings) throws IOException{
        if(usings.size()!=items.size()){
            System.err.println("\"changeUsing\" input size error");
            return;
        }
        for(Map.Entry<Integer,StockItem> entry:currentStocks.entrySet()){
            if((usings.get(entry.getKey())==false)&&(entry.getValue().stock!=0)){
                System.err.println("\"changeUsing\" try to delete "+items.get(entry.getKey())+" which stock is not 0");
                return;
            }
        }
        for (int i=0;i<usings.size();i++){
            if(currentStocks.containsKey(i)&&(usings.get(i)==false)){
                currentStocks.remove(i);
                break;
            }
            if((!currentStocks.containsKey(i))&&(usings.get(i)==true)){
                currentStocks.put(i,new StockItem(items.get(i),0));
                break;
            }
        }
        saveData(this.stock);
    }
    
    public boolean rightOut(List<OutItem> outItems){
        for (OutItem outItem : outItems) {
            if(outItem.item.stock < outItem.outNum){
                System.err.println("“"+outItem.item.name+"”的库存不足！！操作失败！！");
                return false;
            }
        }
        return true;
    }
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException ,ParseException{
        StockData data = new StockData("d:\\data\\stock\\");
        data.init();
        List<List<String>> info=new ArrayList<>();
        info.add(new ArrayList<String>());
        info.get(0).add("0");
        info.get(0).add("93.5");
        info.get(0).add("256");
        info.add(new ArrayList<String>());
        info.get(01).add("1");
        info.get(01).add("99.125");
        info.get(01).add("64");
        data.out(info);
    }
    public static List<String> readAllLines(File fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            List<String> result = new ArrayList<String>();
            for (;;) {
                String line = reader.readLine();
                if (line == null)
                    break;
                result.add(line);
            }
            return result;
        }
    }
    public static void write(File fileName,List<String> content) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String s : content) {
                writer.write(s);
                writer.write("\n");
            }
        }
    }
}

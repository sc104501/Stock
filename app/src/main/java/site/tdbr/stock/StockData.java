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
    List<Item> currentStock;
    Map<Date, List<Item>> stockItemsHis;
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
        currentStock =new ArrayList<>();
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
                    Item item = items.get(Integer.parseInt(line.get(0)));
                    item.stock = Integer.parseInt(line.get(1));
                    item.using = Boolean.parseBoolean(line.get(2));
                }
                break;
            case ("stockHis.txt"):
                for (List<String> line : info) {
                    if (line.size() == 1) {
                        ldt = sdf.parse(line.get(0));
                        stockItemsHis.put(ldt, new ArrayList<Item>());
                    } else {
                        Item item = items.get(Integer.parseInt(line.get(0)));
                        Item stockItem = new Item(item.id, item.stock);
                        stockItem.using = true;
                        stockItem.stock = Integer.parseInt(line.get(1));
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
                                Double.parseDouble(line.get(1)),
                                Integer.parseInt(line.get(2))
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
                                Double.parseDouble(line.get(1)),
                                Integer.parseInt(line.get(2))
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
                for (Item stockItem : items) {
                    lines.add(String.valueOf(stockItem.id) + ","
                            + String.valueOf(stockItem.stock) + ","
                            + String.valueOf(stockItem.using));
                }
                saveContent(this.stock, lines);
                break;
            case ("stockHis.txt"):
                for (Map.Entry<Date, List<Item>> entry : stockItemsHis.entrySet()) {
                    Date ldt = entry.getKey();
                    List<Item> infoList = entry.getValue();
                    lines.add(sdf.format(ldt));
                    for (Item stockHisItem : infoList) {
                        lines.add(String.valueOf(stockHisItem.id) + "," + stockHisItem.stock);
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

    public static double getStockValue(List<Item> items) {
        double sumValue = 0;
        for (Item stockItem : items) {
            if (stockItem.using) {
                sumValue += stockItem.price * stockItem.stock;
            }
        }
        return sumValue;
    }

    public static int getStockNum(List<Item> items) {
        int sumNum = 0;
        for (Item stockItem : items) {
            if (stockItem.using) {
                sumNum += stockItem.stock;
            }
        }
        return sumNum;
    }

    public void in(List<List<String>> info) throws IOException {
        Date ldt = Calendar.getInstance().getTime();
        //start backup
        //append stock info
        List<Item> stockItems = new ArrayList<>();
        for (Item item : this.items) {
            if (item.using) {
                stockItems.add(new Item(item.id,item.stock));
            }
        }
        stockItemsHis.put(ldt, stockItems);
        //append inHis info
        List<InItem> inItems = new ArrayList<>();
        for (List<String> line : info) {
            Item item = items.get(Integer.parseInt(line.get(0)));
            inItems.add(new InItem(item, Double.parseDouble(line.get(1)), Integer.parseInt(line.get(2))));
        }
        inItemsHis.put(ldt, inItems);
        saveData(this.stockHis);
        saveData(this.inHis);
        //start in
        for (InItem inItem : inItems) {
            inItem.item.stock += inItem.inNum;
        }
        saveData(this.stock);
    }

    public void out(List<List<String>> info) throws IOException {
        Date ldt = Calendar.getInstance().getTime();
        //start backup
        //append stock info
        List<Item> stockItems = new ArrayList<>();
        for (Item item : this.items) {
            if (item.using) {
                stockItems.add(new Item(item.id,item.stock));
            }
        }
        stockItemsHis.put(ldt, stockItems);
        //append outHis info
        List<OutItem> outItems = new ArrayList<>();
        for (List<String> line : info) {
            Item item = items.get(Integer.parseInt(line.get(0)));
            outItems.add(new OutItem(item, Double.parseDouble(line.get(1)), Integer.parseInt(line.get(2))));
        }
        outItemsHis.put(ldt, outItems);
        //check
        if(!rightOut(outItems)){
            return;
        }
        saveData(this.stockHis);
        saveData(this.outHis);
        //start out

        for (OutItem outItem : outItems) {
            outItem.item.stock -= outItem.outNum;
        }
        saveData(this.stock);
    }

    public void changeUsing(List<Boolean> using) throws IOException{
        if(using.size()!=items.size()){
            System.err.println("\"changeUsing\" input size error");
            return;
        }
        for (Item stockItem : items) {
            stockItem.using=using.get(stockItem.id);
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

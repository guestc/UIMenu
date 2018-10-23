package cn.guestc.uimenu;

import bsh.Interpreter;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UIMenu extends PluginBase implements Listener {

    private HashMap<String,FormWindowSimple> Forms = new HashMap<>();
    private HashMap<String, ButtonData> Buttons = new HashMap<>();
    private HashMap<String, ElementButtonImageData> Images = new HashMap<>();

    @Override
    public void onEnable() {
        saveResource("data.yml");
        initConfig();
        getServer().getPluginManager().registerEvents(this,this);
    }
    @Override
    public void onLoad(){

    }

    private void initConfig(){
        Config data = new Config(getDataFolder()+"\\"+"data.yml",Config.YAML);
        Map<String,Object> forms,buttons,images;
        forms = (Map<String, Object>) data.get("forms");
        buttons = (Map<String, Object>) data.get("buttons");
        images = (Map<String, Object>) data.get("images");
        for(HashMap.Entry<String,Object> entry : images.entrySet()){
            HashMap<String,String> d = (HashMap<String,String>) entry.getValue();
            ElementButtonImageData img = new ElementButtonImageData(d.get("type"),d.get("data"));
            Images.put(entry.getKey(),img);
        }
        for(HashMap.Entry<String,Object> entry : buttons.entrySet()){
            HashMap<String,String> b = (HashMap<String,String>) entry.getValue();
            ElementButton button = new ElementButton(b.get("text"));
            button.addImage(Images.get(b.get("image")));
            ButtonData bdata = new ButtonData(button,b.get("type"),b.get("data"));
            Buttons.put(entry.getKey(),bdata);
        }
        for(HashMap.Entry<String,Object> entry : forms.entrySet()){
            HashMap<String,Object> f = (HashMap<String,Object>) entry.getValue();
            FormWindowSimple form = new FormWindowSimple(f.get("title").toString(),f.get("text").toString());
            ArrayList<String> bts = (ArrayList<String>) f.get("buttons");
            for(String button : bts){
                form.addButton(Buttons.get(button).button);
            }
            Forms.put(entry.getKey(),form);
        }
        getLogger().warning("data "+Forms.get("menu").getJSONData());
    }

    private String getButtonIdFromText(String text){
        for(HashMap.Entry<String,ButtonData> entry : Buttons.entrySet() ){
            ButtonData bd = entry.getValue();
            if(bd.button.getText().equals(text)) return entry.getKey();
        }
        return null;
    }
    private String getFormIdFromText(String title,String text){
        for(HashMap.Entry<String,FormWindowSimple> entry : Forms.entrySet() ){
            FormWindowSimple form = entry.getValue();
            if(form.getTitle().equals(title) && form.getContent().equals(text)) return entry.getKey();
        }
        return null;
    }

    private void ExecuteButton(String bid,Player player){
        ButtonData bd = Buttons.get(bid);
        String data = bd.data.replace("{playername}",player.getName());
        switch (bd.type){
            case "command":
                getServer().dispatchCommand(player,data);
                break;

            case "form":
                if(Forms.containsValue(data)){
                    player.showFormWindow(Forms.get(data));
                }
                break;

            case "eval":
                try{
                    Interpreter interpreter = new Interpreter();
                    interpreter.eval(data);
                }catch(Exception e){
                    getLogger().warning("execute wrong message: "+e.getMessage());
                }
                break;
        }
    }

    @EventHandler(priority= EventPriority.HIGH,ignoreCancelled=false)
    public void onIntact(PlayerInteractEvent event){
        Player p = event.getPlayer();
        //p.showFormWindow(Forms.get("menu"));
    }

    @EventHandler(priority= EventPriority.HIGH,ignoreCancelled=false)
    public void onResponse(PlayerFormRespondedEvent event){
        Player p = event.getPlayer();
        String name = p.getName();
        if(event.getWindow() instanceof FormWindowSimple){
            FormWindowSimple form = (FormWindowSimple) event.getWindow();
            String fid = getFormIdFromText(form.getTitle(),form.getContent());
            if(fid != null){
                for(ElementButton bt : form.getButtons()){
                    String bid = getButtonIdFromText(bt.getText());
                    if(bid != null) ExecuteButton(bid,p);
                }
            }
        }
    }
}

class ButtonData{
    public ElementButton button;
    public String type;
    public String data;
    public ButtonData(ElementButton bt,String t,String d){
        button = bt;
        type = t;
        data = t;
    }
}

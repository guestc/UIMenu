#UIMenu
```Ymal
forms:
  menu:
    title: "菜单"
    text: "null"
    buttons:
    - "shop"
    - "spawn"
    - "home"
buttons:
  shop:
    type: "command"
    text: "商店"
    data: "plugins"
    image: "shop_icon"
  spawn:
    type: "command"
    text: "主城"
    data: "spawn"
    image: "spawn_icon"
  home:
    type: "command"
    text: "回家"
    data: "home"
    image: "home_icon"
images:
  spawn_icon:
    type: "path"
    data: "textures/blocks/glowing_obsidian.png"
  home_icon:
    type: "path"
    data: "textures/blocks/crafting_table_front.png"
  shop_icon:
    type: "path"
    data: "textures/blocks/chest_front.png"

```
### Form的格式
```Ymal
id:
 title: "标题"
 text: "内容"
 buttons: [按钮ID(buttons里面的ID) 数组]
```

### Button的格式
```Yaml
id:
 type: "按钮类型 command , form , eval"
 text: "按钮的文本"
 data: "数据 {playername} 是玩家名字 <command让玩家执行指令> , <form是打开data里面的form(id)> , <eval 是执行data里面的代码>"
 image: "图标的id 填写Images里面的id"
```
### Image的格式
```Yaml
id:
 type: "类型分path和url"
 data: "图片路径"
```

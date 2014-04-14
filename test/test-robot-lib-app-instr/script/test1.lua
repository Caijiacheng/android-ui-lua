require 'lambda'

log:info("---------begin to lua test1--------------")
launch("org.snail.robot.lib.app.NextAndroidActivity")
assert(oper:waitForAct("NextAndroidActivity"), "failed to wait act, cur Act " .. oper:getCurAct())
oper:sleep(1000)
views = getviewsex() 
log:info("filter btns")
btns = filter(views, function (x) return x:type() == "android.widget.Button" end)
assert(table.getn(btns) == 1, "Activitys has 1 btn")
for i=1,5 do	
	oper:click(btns[1]) 
end
oper:sleep(2000)
log:error("done")

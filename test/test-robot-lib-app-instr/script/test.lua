require 'lambda'

log:info("---------begin to lua test--------------")
launch("org.snail.robot.lib.app.HelloAndroidActivity")
oper:sleep(2000)
views = getviewsex() 
dump(views)
log:info("filter btns")
btns = filter(views, function (x) return tonumber(x:text()) ~= nil end)
assert(table.getn(btns) == 3, "Activitys has 3 btns with numbers")
dump(btns)
for i=1,3 do	
	map(btns, function (x) return oper:click(x) end) 
	log:error("click {}", i)
	oper:sleep(200)
end
btn_next = filter(views, function (x) return x:text() == "Next" end)
assert(table.getn(btn_next) == 1, "go bo next activity")
oper:click(btn_next[1])
assert(oper:waitForAct("NextAndroidActivity"), "failed to wait act, cur Act " .. oper:getCurAct())
oper:sleep(2000)
log:error("done")

function getviewsex()
	log:error("getviews")
	local viewset=getviews()
	local t = {}
	for i=1, viewset:size() do
		t[i] = viewset:get(i-1)
	end
	return t
end
function filter(views, ...)
	local t = views
	for _, v in ipairs(arg) do
		local tt = {}
		local cnt = 1
		for _, vv in ipairs(t) do
			if v(vv) == true then
				tt[cnt] = vv
				cnt = cnt + 1
			end
		end
		t = tt
	end
	return t
end

function map(views, ...)
	local t = views
	for _, v in ipairs(arg) do
		local tt = {}
		local cnt = 1
		for _, vv in ipairs(t) do
			tt[cnt] = v(vv)
			cnt = cnt + 1
		end
		t = tt
	end
	return t
end

function dump(views)
	for i, v in ipairs(views) do
		log:info("view[{}] = {}", i, v)
	end	
end
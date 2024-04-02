local indexName = KEYS[1]
local exists = redis.call('ft.info', KEYS[1])
for i = 1, #KEYS do
    redis.call('JSON.SET', KEYS[i], '$', ARGV[i])
end
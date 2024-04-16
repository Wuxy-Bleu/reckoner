            for i = 1, #KEYS do
                redis.call('JSON.SET', KEYS[i], '$', ARGV[i])
                redis.call('EXPIRE', KEYS[i], tonumber(ARGV[#ARGV]))
            end
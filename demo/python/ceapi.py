
import hmac
import hashlib
import time
import json
import requests


url         = "https://api.coinegg.im"
private_key = "zx6pA-hXm7/-sBg~z-nCiP4-[Dr;H-[Uk25-mSOkK"
public_key  = "wb55j-jrdw8-byghj-n8b8y-zcc3y-r88z9-i7ykm"

def hmac_sha256(access_key, params):
    str = params.encode("utf-8")
    md5_key = md5_sign(access_key)
    hmac_obj = hmac.new(md5_key.encode('utf-8'), str, digestmod=hashlib.sha256);

    return hmac_obj.hexdigest().lower()

def md5_sign(str):
    hl_obj = hashlib.md5()
    hl_obj.update(str.encode("utf-8"))
    return hl_obj.hexdigest().lower()

def get_signature(params):
    params= sorted(params.items(), key=lambda item:item[0])
    params_str = ""
    for i in params:
        params_str += i[0]+"="+str(i[1])+"&"

    params_str = params_str.rstrip('&')
    return hmac_sha256(private_key, params_str)

def getBalance():
    params = {
            'key': public_key,
            }

    params['signature'] = get_signature(params)
    print("signature:"+params['signature'])
    params = OrderedDict(sorted(params.items(), key=lambda item:item[0]))
    response = requests.post(url+"/api/v1/balance/", data = params)
    return response.text

print(getBalance())

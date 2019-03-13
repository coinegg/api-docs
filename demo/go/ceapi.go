
package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"strings"
	"crypto/hmac"
	"crypto/sha256"
	"crypto/md5"
	"encoding/hex"
)
//网站域名
var base_url    = "https://api.coinegg.im"
//公钥
var public_key  = "wb55j-jrdw8-byghj-n8b8y-zcc3y-r88z9-i7ykm"
//私钥
var private_key = md5Encry("zx6pA-hXm7/-sBg~z-nCiP4-[Dr;H-[Uk25-mSOkK")

func main(){
	//获取账户余额
	getBalance();
}
	
//获取账户余额	
func getBalance(){

	var url = base_url + "/api/v1/balance/"
	params := "key="+public_key
	params += "&signature="+getSha256Encry(params)
	result := httpRequest("POST", url, params)

	fmt.Println(result)
}

//sha 256加密
func getSha256Encry(str string) string{
	privateKeyByte := []byte(private_key)
	h := hmac.New(sha256.New, privateKeyByte)
	h.Write([]byte(str))
	return hex.EncodeToString(h.Sum(nil))
}

//md5加密
func md5Encry(text string) string {
   ctx := md5.New()
   ctx.Write([]byte(text))
   return hex.EncodeToString(ctx.Sum(nil))
}

//http请求
func httpRequest(method string, url string, params string) string {
    request, _ := http.NewRequest(method, url, strings.NewReader(params))
    request.Header.Set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")

    response,err := http.DefaultClient.Do(request)

    if err != nil {
        fmt.Printf("http error:%v\n", err)
        return ""
    } else {
        respBody,_ := ioutil.ReadAll(response.Body)
        return string(respBody)
    }
}
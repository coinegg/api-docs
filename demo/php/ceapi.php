<?php
error_reporting(E_ALL);

$balance = coinEggApi::getBalance();

var_dump($balance);

class coinEggApi
{
    const PUBLIC_KEY  = "wb55j-jrdw8-byghj-n8b8y-zcc3y-r88z9-i7ykm";
    const PRIVATE_KEY = "zx6pA-hXm7/-sBg~z-nCiP4-[Dr;H-[Uk25-mSOkK";
    const URL         = "https://api.coinegg.im";

    public static function getBalance()
    {
        $url         = self::URL . "/api/v1/balance/";

        $data        = [];
        $data['key'] = self::PUBLIC_KEY;
        ksort($data);
        $data['signature'] = hash_hmac('sha256', http_build_query($data, '', '&'), md5(self::PRIVATE_KEY));

        $result = self::httpRequest($url, "post", $data);
        var_dump($result);
    }

    public static function httpRequest($url, $method, $data)
    {
        $curl = curl_init();

        curl_setopt($curl, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
        curl_setopt($curl, CURLOPT_CONNECTTIMEOUT, 30);
        curl_setopt($curl, CURLOPT_TIMEOUT, 30);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curl, CURLOPT_ENCODING, '');
        curl_setopt($curl, CURLOPT_HEADER, false);
        $method = strtoupper($method);
        if ('GET' === $method) {
            if ($data !== null) {
                if (strpos($url, '?')) {
                    $url .= '&';
                } else {
                    $url .= '?';
                }
                $url .= http_build_query($data);
            }
        } elseif ('POST' === $method) {
            curl_setopt($curl, CURLOPT_POST, true);
            if (!empty($data)) {
                if (is_string($data)) {
                    curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
                } else {
                    curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data));
                }
            }
        }

        $isSsl = substr($url, 0, 8) == "https://" ? true : false;
        if ($isSsl) {
            curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false); // 信任任何证书
            curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 2); // 检查证书中是否设置域名
        }
        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLINFO_HEADER_OUT, true);

        $response = curl_exec($curl);
        curl_close($curl);

        return $response;
    }
}
function ceapi() {
        var publicKey = "wb55j-jrdw8-byghj-n8b8y-zcc3y-r88z9-i7ykm";
        var privateKey = "zx6pA-hXm7/-sBg~z-nCiP4-[Dr;H-[Uk25-mSOkK";
        var url = "https://api.coinegg.im/api/v1/balance/";

        var params = "key=" + publicKey;

        var md5PriKey = $.md5(privateKey);

        console.log(md5PriKey);
        var signature = hex_hmac_sha256(md5PriKey, params);

        console.log(signature);

        $.ajax({
            url:'https://api.coinegg.im/api/v1/balance',
            type: "POST",
            data:{'key':publicKey, 'signature': signature},
            dataType: "json",
            success: function(data){

            }
        });




    }
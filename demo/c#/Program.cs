using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security.Cryptography;
using System.Net;
using System.IO;

namespace ConsoleApplication1
{
    class Program
    {
        static void Main(string[] args)
        {
            //这个账号0资产
            string publicKey = "uuws1-3pgqu-dw5i4-b7w1x-32pp6-j74ay-95s6w";
            string privateKey = "]jMj!-zO@Ka-){t@^-n(()P-8H/h;-]D*JQ-.s{yL";
            string url = "http://www.jubi.com/api/v1/balance/";
            //nonce 值每次都不可以相同,可以自己做值记录,每次加1 也可以用毫秒的时间戳
            long nonce = timeMillis();
            string postparams = "key=" + publicKey + "&nonce=" + nonce;
            string key = MD5(privateKey).ToLower();
            string signature = HmacSha256(postparams, key);
            postparams = postparams + "&signature=" + signature;
            string result = HttpPostData(url, postparams);
            Console.WriteLine(result);
            Console.ReadLine();
        }



        private static long timeMillis()
        {
            TimeSpan ts = new TimeSpan(DateTime.Now.Ticks);
            return (long)ts.TotalMilliseconds;
        }

        public static string MD5(string myString)
        {
            byte[] data = Encoding.GetEncoding("GB2312").GetBytes(myString);
             MD5 md5 = new MD5CryptoServiceProvider();
             byte[] OutBytes = md5.ComputeHash(data);
 
             string OutString = "";
             for (int i = 0; i < OutBytes.Length; i++)
             {
                 OutString += OutBytes[i].ToString("x2");
             }
            // return OutString.ToUpper();
             return OutString.ToLower();

        }

        public static string HmacSha256(string word, string key)
        {

            HMACSHA256 mySha256 = new HMACSHA256();
            mySha256.Key = System.Text.Encoding.ASCII.GetBytes(key);
            byte[] myWord = System.Text.Encoding.Default.GetBytes(word);
            byte[] result = mySha256.ComputeHash(myWord);
            StringBuilder myResult = new StringBuilder();
            foreach (byte myChar in result)
            {
                myResult.AppendFormat("{0:x2}", myChar);
            }
            return myResult.ToString();
        }


        public static string HttpPostData(string url, string param)
        {
            var result = string.Empty;
            //注意提交的编码 这边是需要改变的 这边默认的是Default：系统当前编码
            byte[] postData = Encoding.Default.GetBytes(param);

            // 设置提交的相关参数 
            HttpWebRequest request = WebRequest.Create(url) as HttpWebRequest;
            Encoding myEncoding = Encoding.Default;
            request.Method = "POST";
            request.KeepAlive = false;
            request.AllowAutoRedirect = true;
            request.ContentType = "application/x-www-form-urlencoded";
            request.ContentLength = postData.Length;
            // 提交请求数据 
            System.IO.Stream outputStream = request.GetRequestStream();
            outputStream.Write(postData, 0, postData.Length);
            outputStream.Close();
            HttpWebResponse response;
            Stream responseStream;
            StreamReader reader;
            string srcString;
            response = request.GetResponse() as HttpWebResponse;
            responseStream = response.GetResponseStream();
            reader = new System.IO.StreamReader(responseStream, Encoding.GetEncoding("UTF-8"));
            srcString = reader.ReadToEnd();
            result = srcString;   //返回值赋值
            reader.Close();
            return result;
        }
    }
}

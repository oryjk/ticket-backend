package com.carlwang.ticketcloudserver.ticket.config

class RequestDataStore {
    val createMatchOrderRequest="""
        {
        	"encryptedData": "754621e716125780928da9ec3f5ab4b0821efae5af5009c0e2f298e3d2592fba984b83d48c0145490235f6a990c017855d3234f79fb6ef11e3c36afc38403fa51c9b8077c25d6c61f0b004cd0c96bf7d1e1d2206d54174106c4ab667531f781735bf1730693385d7afabba010a2c7bbaf275eab134ad15514519177cdb35ab2202e479da4133a0319e7dcf00b909605cb981efd80c3d3646b0d9944fa55b9ac496ce895b93074296cf4a1753ffa9c19fb1f2a6a4ac9bcd7eaba076c232097446f1b920681fa32c8ca8e5cddc50f0568267e49fe0b247bb4065cd102a869259057a56f37af82cb13d9690ce1e3c2bfeabe018a2e1e317d4b1f5e4aa076c224529de6bc7c0e4bc69033a2fc09635e49e19040f4f2e2d990bda550d0e08592624da9e8d05def573bcd4e0ce8df9bf451b446bf1559a45134d1586716af764c96ebff1e7a5a07aa85f6bf1837a490833fbc4707dcda28dc7e606abc024decd175bb664c3aff0c8355e8459bda3516eb88c257ca4917075f4e26845d97d7283e85d45735bc48bc220574ea4e92138f13f0978977f133173f590b3f384d702145c02837b118c41dc839044577d7cc53f1a03b675fcc66a80826dddec59c98453193d062046989f464d12e121f3bcd93e4d9191",
        	"version": 24,
        	"expireTime": 1713960614427,
        	"agree": true,
        	"id": "24",
        	"regions": [{
        		"region": 6,
        		"estate": 4,
        		"num": 1,
        		"name": "504",
        		"price": "100.00",
        		"usable_count": "200"
        	}],
        	"users": [{
        		"id": 116692,
        		"uid": 116692,
        		"realname": "王睿",
        		"real_card_id": "510***********6011",
        		"phone": "186****2970",
        		"is_self": true,
        		"real_card_id2": "510112198905246011",
        		"phone2": "18602812970",
        		"timestamp": 1713956648,
        		"signature": "77fa6798ab5c1f1c352557ed11b6dcf6",
        		"disabled": false,
        		"disabled2": false,
        		"showText": "王睿 510***********6011"
        	}]
        }
    """.trimIndent()

    val createMatchOrderResponse="""
        {
        	"code": 5,
        	"msg": "抢座失败，请稍后再试！",
        	"timeout": 8
        }
    """.trimIndent()


}
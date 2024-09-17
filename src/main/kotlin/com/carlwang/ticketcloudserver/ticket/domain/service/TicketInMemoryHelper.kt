package com.carlwang.ticketcloudserver.ticket.domain.service

object TicketInMemoryHelper {
    val responseJson = """
       {
       	"btn_status": 1,
       	"btn_text": "立即购买",
       	"code": 1,
       	"data": [{
       		"usable_count": 7322,
       		"max_price": "400.00",
       		"min_price": "180.00",
       		"region": 1,
       		"list": [{
       			"usable_count": 836,
       			"price": "180.00",
       			"estate": 1,
       			"name": "101",
       			"id": 1
       		}, {
       			"usable_count": 458,
       			"price": "180.00",
       			"estate": 2,
       			"name": "102",
       			"id": 2
       		}, {
       			"usable_count": 349,
       			"price": "180.00",
       			"estate": 3,
       			"name": "103",
       			"id": 3
       		}, {
       			"usable_count": 264,
       			"price": "180.00",
       			"estate": 4,
       			"name": "104",
       			"id": 4
       		}, {
       			"usable_count": 359,
       			"price": "180.00",
       			"estate": 5,
       			"name": "105",
       			"id": 5
       		}, {
       			"usable_count": 475,
       			"price": "180.00",
       			"estate": 6,
       			"name": "106",
       			"id": 6
       		}, {
       			"usable_count": 618,
       			"price": "180.00",
       			"estate": 7,
       			"name": "107",
       			"id": 7
       		}, {
       			"usable_count": 389,
       			"price": "180.00",
       			"estate": 8,
       			"name": "108",
       			"id": 8
       		}, {
       			"usable_count": 402,
       			"price": "220.00",
       			"estate": 9,
       			"name": "109",
       			"id": 9
       		}, {
       			"usable_count": 493,
       			"price": "220.00",
       			"estate": 10,
       			"name": "110",
       			"id": 10
       		}, {
       			"usable_count": 128,
       			"price": "220.00",
       			"estate": 11,
       			"name": "111",
       			"id": 11
       		}, {
       			"usable_count": 120,
       			"price": "220.00",
       			"estate": 12,
       			"name": "112",
       			"id": 12
       		}, {
       			"usable_count": 452,
       			"price": "220.00",
       			"estate": 13,
       			"name": "113",
       			"id": 13
       		}, {
       			"usable_count": 593,
       			"price": "220.00",
       			"estate": 25,
       			"name": "125",
       			"id": 25
       		}, {
       			"usable_count": 173,
       			"price": "220.00",
       			"estate": 26,
       			"name": "126",
       			"id": 26
       		}, {
       			"usable_count": 82,
       			"price": "400.00",
       			"estate": 27,
       			"name": "127",
       			"id": 27
       		}, {
       			"usable_count": 196,
       			"price": "400.00",
       			"estate": 29,
       			"name": "129",
       			"id": 29
       		}, {
       			"usable_count": 431,
       			"price": "220.00",
       			"estate": 30,
       			"name": "130",
       			"id": 30
       		}, {
       			"usable_count": 504,
       			"price": "220.00",
       			"estate": 31,
       			"name": "131",
       			"id": 31
       		}],
       		"name": "首层看台"
       	}, {
       		"usable_count": 281,
       		"max_price": "1288.00",
       		"min_price": "1288.00",
       		"region": 3,
       		"list": [{
       			"usable_count": 129,
       			"price": "1288.00",
       			"estate": 1,
       			"name": "VIP1",
       			"id": 1
       		}, {
       			"usable_count": 152,
       			"price": "1288.00",
       			"estate": 3,
       			"name": "VIP3",
       			"id": 3
       		}],
       		"name": "VIP"
       	}, {
       		"usable_count": 11983,
       		"max_price": "180.00",
       		"min_price": "100.00",
       		"region": 6,
       		"list": [{
       			"usable_count": 608,
       			"price": "100.00",
       			"estate": 3,
       			"name": "503",
       			"id": 3
       		}, {
       			"usable_count": 417,
       			"price": "100.00",
       			"estate": 4,
       			"name": "504",
       			"id": 4
       		}, {
       			"usable_count": 111,
       			"price": "100.00",
       			"estate": 5,
       			"name": "505",
       			"id": 5
       		}, {
       			"usable_count": 383,
       			"price": "100.00",
       			"estate": 6,
       			"name": "506",
       			"id": 6
       		}, {
       			"usable_count": 565,
       			"price": "100.00",
       			"estate": 7,
       			"name": "507",
       			"id": 7
       		}, {
       			"usable_count": 647,
       			"price": "120.00",
       			"estate": 8,
       			"name": "508",
       			"id": 8
       		}, {
       			"usable_count": 604,
       			"price": "120.00",
       			"estate": 10,
       			"name": "510",
       			"id": 10
       		}, {
       			"usable_count": 159,
       			"price": "150.00",
       			"estate": 11,
       			"name": "511",
       			"id": 11
       		}, {
       			"usable_count": 266,
       			"price": "150.00",
       			"estate": 12,
       			"name": "512",
       			"id": 12
       		}, {
       			"usable_count": 369,
       			"price": "180.00",
       			"estate": 13,
       			"name": "513",
       			"id": 13
       		}, {
       			"usable_count": 196,
       			"price": "180.00",
       			"estate": 14,
       			"name": "514",
       			"id": 14
       		}, {
       			"usable_count": 330,
       			"price": "180.00",
       			"estate": 15,
       			"name": "515",
       			"id": 15
       		}, {
       			"usable_count": 465,
       			"price": "150.00",
       			"estate": 16,
       			"name": "516",
       			"id": 16
       		}, {
       			"usable_count": 522,
       			"price": "150.00",
       			"estate": 17,
       			"name": "517",
       			"id": 17
       		}, {
       			"usable_count": 580,
       			"price": "120.00",
       			"estate": 18,
       			"name": "518",
       			"id": 18
       		}, {
       			"usable_count": 625,
       			"price": "120.00",
       			"estate": 20,
       			"name": "520",
       			"id": 20
       		}, {
       			"usable_count": 409,
       			"price": "100.00",
       			"estate": 21,
       			"name": "521",
       			"id": 21
       		}, {
       			"usable_count": 229,
       			"price": "100.00",
       			"estate": 22,
       			"name": "522",
       			"id": 22
       		}, {
       			"usable_count": 143,
       			"price": "100.00",
       			"estate": 23,
       			"name": "523",
       			"id": 23
       		}, {
       			"usable_count": 257,
       			"price": "100.00",
       			"estate": 24,
       			"name": "524",
       			"id": 24
       		}, {
       			"usable_count": 250,
       			"price": "100.00",
       			"estate": 25,
       			"name": "525",
       			"id": 25
       		}, {
       			"usable_count": 665,
       			"price": "120.00",
       			"estate": 26,
       			"name": "526",
       			"id": 26
       		}, {
       			"usable_count": 514,
       			"price": "120.00",
       			"estate": 28,
       			"name": "528",
       			"id": 28
       		}, {
       			"usable_count": 512,
       			"price": "150.00",
       			"estate": 29,
       			"name": "529",
       			"id": 29
       		}, {
       			"usable_count": 376,
       			"price": "150.00",
       			"estate": 30,
       			"name": "530",
       			"id": 30
       		}, {
       			"usable_count": 346,
       			"price": "180.00",
       			"estate": 31,
       			"name": "531",
       			"id": 31
       		}, {
       			"usable_count": 328,
       			"price": "180.00",
       			"estate": 32,
       			"name": "532",
       			"id": 32
       		}, {
       			"usable_count": 476,
       			"price": "150.00",
       			"estate": 34,
       			"name": "534",
       			"id": 34
       		}, {
       			"usable_count": 631,
       			"price": "150.00",
       			"estate": 35,
       			"name": "535",
       			"id": 35
       		}],
       		"name": "顶层看台"
       	}],
       	"msg": "获取成功",
       	"max_num": 2,
       	"type_code": 1
       }
    """.trimIndent()
}
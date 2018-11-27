curl -XPUT localhost:9200/_template/dapp?pretty -d'{
	"template": "dapp_*",
	"aliases": {
		"{index}_alias": {
			
		}
	},
	"mappings": {
		"account_related_trx_info": {
			"_all": {
				"enabled": false
			},
			"properties": {
				"trx_id": {
					"type": "keyword"
				},
				"timestamp": {
					"type": "date"
				},
				"receiver": {
					"type": "keyword"
				},
				"sender": {
					"type": "keyword"
				},
				"code": {
					"type": "keyword"
				},
				"quantity": {
					"type": "keyword"
				},
				"memo": {
					"type": "keyword"
				},
				"symbol": {
					"type": "keyword"
				},
				"status": {
					"type": "keyword"
				}
			}
		}
	}
}'
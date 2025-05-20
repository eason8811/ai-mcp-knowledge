curl https://apis.itedus.cn/v1/chat/completions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer sk-rJlvgIfxS1XHgCHPB638Fb13232848B69fC3C431AfF919Ca" \
  -d '{
    "model": "gpt-4.1-mini",
    "messages": [
      {
        "role": "user",
        "content": "1+1"
      }
    ]
  }'
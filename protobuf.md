# Protocol Documentation
<a name="top"></a>

## Table of Contents

- [wallet.proto](#wallet.proto)
    - [BalanceSummary](#wallet.BalanceSummary)
    - [BalanceSummary.AmountEntry](#wallet.BalanceSummary.AmountEntry)
    - [WalletRequest](#wallet.WalletRequest)
  
    - [CURRENCY](#wallet.CURRENCY)
  
  
    - [Wallet](#wallet.Wallet)
  

- [Scalar Value Types](#scalar-value-types)



<a name="wallet.proto"></a>
<p align="right"><a href="#top">Top</a></p>

## wallet.proto



<a name="wallet.BalanceSummary"></a>

### BalanceSummary



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| amount | [BalanceSummary.AmountEntry](#wallet.BalanceSummary.AmountEntry) | repeated |  |






<a name="wallet.BalanceSummary.AmountEntry"></a>

### BalanceSummary.AmountEntry



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| key | [string](#string) |  |  |
| value | [string](#string) |  |  |






<a name="wallet.WalletRequest"></a>

### WalletRequest



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| user | [int64](#int64) |  |  |
| amount | [string](#string) |  |  |
| currency | [CURRENCY](#wallet.CURRENCY) |  |  |





 


<a name="wallet.CURRENCY"></a>

### CURRENCY


| Name | Number | Description |
| ---- | ------ | ----------- |
| USD | 0 |  |
| EUR | 1 |  |
| GBP | 2 |  |


 

 


<a name="wallet.Wallet"></a>

### Wallet


| Method Name | Request Type | Response Type | Description |
| ----------- | ------------ | ------------- | ------------|
| Deposit | [WalletRequest](#wallet.WalletRequest) | [.google.protobuf.Empty](#google.protobuf.Empty) |  |
| Withdraw | [WalletRequest](#wallet.WalletRequest) | [.google.protobuf.Empty](#google.protobuf.Empty) |  |
| Balance | [WalletRequest](#wallet.WalletRequest) | [BalanceSummary](#wallet.BalanceSummary) |  |

 



## Scalar Value Types

| .proto Type | Notes | C++ Type | Java Type | Python Type |
| ----------- | ----- | -------- | --------- | ----------- |
| <a name="double" /> double |  | double | double | float |
| <a name="float" /> float |  | float | float | float |
| <a name="int32" /> int32 | Uses variable-length encoding. Inefficient for encoding negative numbers – if your field is likely to have negative values, use sint32 instead. | int32 | int | int |
| <a name="int64" /> int64 | Uses variable-length encoding. Inefficient for encoding negative numbers – if your field is likely to have negative values, use sint64 instead. | int64 | long | int/long |
| <a name="uint32" /> uint32 | Uses variable-length encoding. | uint32 | int | int/long |
| <a name="uint64" /> uint64 | Uses variable-length encoding. | uint64 | long | int/long |
| <a name="sint32" /> sint32 | Uses variable-length encoding. Signed int value. These more efficiently encode negative numbers than regular int32s. | int32 | int | int |
| <a name="sint64" /> sint64 | Uses variable-length encoding. Signed int value. These more efficiently encode negative numbers than regular int64s. | int64 | long | int/long |
| <a name="fixed32" /> fixed32 | Always four bytes. More efficient than uint32 if values are often greater than 2^28. | uint32 | int | int |
| <a name="fixed64" /> fixed64 | Always eight bytes. More efficient than uint64 if values are often greater than 2^56. | uint64 | long | int/long |
| <a name="sfixed32" /> sfixed32 | Always four bytes. | int32 | int | int |
| <a name="sfixed64" /> sfixed64 | Always eight bytes. | int64 | long | int/long |
| <a name="bool" /> bool |  | bool | boolean | boolean |
| <a name="string" /> string | A string must always contain UTF-8 encoded or 7-bit ASCII text. | string | String | str/unicode |
| <a name="bytes" /> bytes | May contain any arbitrary sequence of bytes. | string | ByteString | str |


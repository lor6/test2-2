package com.baeldung.lombok.accessors.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Accessors(prefix = {"s", "bd"})
@Getter
@Setter
public class PrefixedAccount {
    String sName;
    BigDecimal bdBalance;
}

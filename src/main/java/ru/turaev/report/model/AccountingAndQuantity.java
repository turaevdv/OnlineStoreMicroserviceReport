package ru.turaev.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingAndQuantity {
    private long accountingId;
    private int quantity;
}

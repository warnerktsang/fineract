/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.cob.conditions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.mock.env.MockEnvironment;

@ExtendWith(MockitoExtension.class)
class LoanCOBWorkerConditionTest {

    @Mock
    private ConditionContext conditionContext;

    @Mock
    private AnnotatedTypeMetadata metadata;

    private MockEnvironment environment;

    @InjectMocks
    private LoanCOBWorkerCondition testObj = new LoanCOBWorkerCondition();

    @BeforeEach
    public void setUp() {
        environment = new MockEnvironment();
        given(conditionContext.getEnvironment()).willReturn(environment);
    }

    @Test
    public void testMatchesShouldReturnFalseWhenLoanCobIsDisabledAndWorker() {
        environment.withProperty("fineract.job.loan-cob-enabled", "false");
        environment.withProperty("fineract.mode.batch-worker-enabled", "true");
        boolean result = testObj.matches(conditionContext, metadata);
        assertThat(result).isFalse();
    }

    @Test
    public void testMatchesShouldReturnFalseWhenLoanCobIsEnabledAndNotWorker() {
        environment.withProperty("fineract.job.loan-cob-enabled", "true");
        environment.withProperty("fineract.mode.batch-worker-enabled", "false");
        boolean result = testObj.matches(conditionContext, metadata);
        assertThat(result).isFalse();
    }

    @Test
    public void testMatchesShouldReturnTrueWhenLoanCobIsEnabledAndWorker() {
        environment.withProperty("fineract.job.loan-cob-enabled", "true");
        environment.withProperty("fineract.mode.batch-worker-enabled", "true");
        boolean result = testObj.matches(conditionContext, metadata);
        assertThat(result).isTrue();
    }
}
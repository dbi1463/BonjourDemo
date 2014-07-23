//
//  BonjourDiscoveredServiceCell.m
//  BonjouriOS
//
//  Created by Spirit on 7/23/14.
//  Copyright (c) 2014 funymph. All rights reserved.
//

#import "BonjourDiscoveredServiceCell.h"

NSString* const BonjourDiscoveredServiceCellIdentifier = @"BonjourDiscoveredServiceCell";

@implementation BonjourDiscoveredServiceCell {
	NSNetService* _service;
}

- (NSNetService*)service {
	return _service;
}

- (void)setService:(NSNetService*)service {
	_service = service;
	self.serviceNameLabel.text = service.name;
}

@end
